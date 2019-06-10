package com.xiongchao.blog.controller;


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
            Follow follow0 = followService.findByUserIdAndFollowUserId(userId, follow.getFollowUserId());
            if (follow0 != null){
                return BaseResult.failure("该用户已被关注");
            }
            follow.setFollowUserId(user.getId());
            follow.setNickname(user.getNickname());
            follow.setAvatar(user.getAvatar());
            // 添加被关注用户的粉丝
            Follow follow1 = new Follow();
            User user1 = userService.findById(userId).orElseThrow(()->new RuntimeException("被关注用户不存在"));
            follow1.setAvatar(user1.getAvatar());
            follow1.setName(user1.getName());
            follow1.setNickname(user1.getNickname());
            follow1.setFollowUserId(userId);
            follow1.setStatus(2);
            follow1.setUserId(follow.getFollowUserId());
            followService.saveFollow(follow1);
        }
        followService.saveFollow(follow);
        return BaseResult.success();
    }

    @PostMapping("updateStatus")
    @ApiOperation("修改关注状态")
    public BaseResult updateStatus(@RequestParam("id") Integer id,
                                   @RequestParam("status") @ApiParam(allowableValues = "0,1,2", value = "0:删除;1:关注;2：粉丝") Integer status,
                             @ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer userId) {
        Follow follow = followService.findById(id).orElseThrow(()-> new RuntimeException("被关注用户不存在，无法修改数据"));
        // 修改关注状态
        follow.setStatus(status);
        followService.saveFollow(follow);
        // 修改被关注用户的状态（修改粉丝状态）
        Follow follow1 = followService.findByUserIdAndFollowUserId(follow.getFollowUserId(), userId);
        int status1;
        if(status == 0){
            status1 = 0;
        } else if(status == 1){
            status1 = 2;
        } else {
            status1 =1;
        }
        follow1.setStatus(status1);
        followService.saveFollow(follow1);
        return BaseResult.success();
    }

    @PostMapping("updateNickName")
    @ApiOperation("修改关注者昵称")
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
