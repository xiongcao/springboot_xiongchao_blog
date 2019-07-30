package com.xiongchao.blog.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiongchao.blog.bean.*;
import com.xiongchao.blog.service.FollowService;
import com.xiongchao.blog.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Api(description = "我的关注")
@RestController
@RequestMapping("follow")
public class FollowController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Logger loggerUser = LoggerFactory.getLogger("ADMIN");

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @PostMapping("save")
    @ApiOperation("添加关注用户")
    public BaseResult save(@Valid @RequestBody Follow follow,
                             @ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer userId) {
        User user = userService.findById(follow.getFollowUserId()).orElseThrow(()->new RuntimeException("被关注用户不存在"));
        follow.setUserId(userId);
        follow.setStatus(1);
        follow.setName(user.getName());
        if(follow.getId() == null){
            Follow follow0 = followService.findByUserIdAndFollowUserId(userId, follow.getFollowUserId(), 1);
            if (follow0 != null){
                return BaseResult.failure("该用户已被关注");
            }
            follow0 = followService.findByUserIdAndFollowUserId(userId, follow.getFollowUserId(), 0);
            if (follow0 != null) {  // 该用户已被关注过
                follow.setId(follow0.getId());
                follow = JSONObject.parseObject(JSON.toJSONString(follow0), Follow.class);
                follow.setStatus(1);
                // 查询被关注的用户的粉丝
                Follow fans = followService.findByUserIdAndFollowUserId(follow.getFollowUserId(), userId, 0);
                fans.setStatus(2);
                followService.saveFollow(fans);
            } else {
                follow.setFollowUserId(user.getId());
                follow.setNickname(user.getNickname());
                follow.setAvatar(user.getAvatar());
                // 添加被关注用户的粉丝
                Follow fans = new Follow();
                User user1 = userService.findById(userId).orElseThrow(()->new RuntimeException("被关注用户不存在"));
                fans.setAvatar(user1.getAvatar());
                fans.setName(user1.getName());
                fans.setNickname(user1.getNickname());
                fans.setFollowUserId(userId);
                fans.setStatus(2);
                fans.setUserId(follow.getFollowUserId());
                followService.saveFollow(fans);
            }
        }
        followService.saveFollow(follow);
        return BaseResult.success();
    }

    @PostMapping("unFollow/{id}")
    @ApiOperation("取消关注")
    public BaseResult unFollow(@PathVariable("id") Integer id,
                               @ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer userId){
        Follow follow = followService.findById(id).orElseThrow(()-> new RuntimeException("被关注用户不存在，无法修改数据"));
        // 修改关注状态
        follow.setStatus(0);
        followService.saveFollow(follow);
        // 修改被关注用户的状态（删除粉丝）
        Follow follow1 = followService.findByUserIdAndFollowUserId(follow.getFollowUserId(), userId, 2);
        follow1.setStatus(0);
        followService.saveFollow(follow1);
        return BaseResult.success();
    }

    @PostMapping("updateNickName")
    @ApiOperation("修改关注者备注")
    public BaseResult updateNickName(@RequestParam("id") Integer id,
                                     @RequestParam("nickname") String nickname,
                                   @ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer userId) {
        Follow follow = followService.findById(id).orElseThrow(()-> new RuntimeException("被关注用户不存在，无法修改数据"));
        follow.setNickname(nickname);
        followService.saveFollow(follow);
        return BaseResult.success();
    }

    @GetMapping("followList/{status}")
    @ApiOperation("查询关注列表")
    public BaseResult list(PageWithSearch page,
                           @ApiParam("0:删除;1:关注;2：粉丝") @PathVariable("status") Integer status,
                           @ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer userId) {
        if(status == null) return BaseResult.failure("status必传");
        return BaseResult.success(followService.findFollowAll(page, userId, status));
    }
}
