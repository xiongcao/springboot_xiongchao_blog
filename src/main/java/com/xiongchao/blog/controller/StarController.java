package com.xiongchao.blog.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiongchao.blog.DTO.CollectDTO;
import com.xiongchao.blog.bean.*;
import com.xiongchao.blog.service.EssayService;
import com.xiongchao.blog.service.StarService;
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
import java.util.ArrayList;
import java.util.List;

@Api(description = "点赞管理")
@RestController
@RequestMapping("star")
public class StarController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Logger loggerUser = LoggerFactory.getLogger("ADMIN");

    @Autowired
    private StarService starService;

    @Autowired
    private UserService userService;

    @Autowired
    private EssayService essayService;

    @PostMapping("save")
    @ApiOperation("保存")
    public BaseResult save(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                           @Valid @RequestBody Star star) {
        if(star.getId() != null){
            Star star1 = starService.findByIdAndUserId(star.getId(), adminId);
            if (null == star1) {
                return BaseResult.failure("没有查询到");
            }
        }
        star.setUserId(adminId);
        starService.save(star);
        Essay essay = essayService.findById(star.getEssayId()).orElseThrow(()-> new RuntimeException("没有查到次文章"));
        if (star.getStatus() == 1) { // 点赞
            essay.setStarCount(essay.getStarCount() + 1);
        } else {  // 取消点赞
            essay.setStarCount(essay.getStarCount() - 1);
        }
        essayService.save(essay);
        return BaseResult.success(star);
    }

    @PostMapping("updateStatus/{id}/{status}")
    @ApiOperation("修改状态")
    public BaseResult updateStatus(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                                   @ApiParam("ID") @PathVariable("id") Integer id,
                                   @ApiParam("0:删除;1:正常;") @PathVariable("status") Integer status) {
        Star star = starService.findById(id).orElseThrow(()-> new RuntimeException("没有查询到"));
        star.setStatus(status);
        starService.save(star);
        return BaseResult.success();
    }

    @GetMapping("findAll")
    @ApiOperation("查询所有点赞文章")
    public BaseResult findAll(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId) {
        List<CollectDTO> collectDTOS = new ArrayList<>();
        List<Star> stars  = starService.findAllByUserId(adminId);
        for (Star star: stars) {
            CollectDTO collectDTO = JSONObject.parseObject(JSON.toJSONString(star), CollectDTO.class);
            Essay essay = essayService.findById(star.getEssayId()).orElseThrow(() ->  new RuntimeException("文章不存在"));
            collectDTO.setTitle(essay.getTitle());
            User user = userService.findById(essay.getUserId()).orElseThrow(() ->  new RuntimeException("用户不存在"));
            collectDTO.setName(user.getName());
            collectDTO.setNickname(user.getNickname());
            collectDTO.setRemark(user.getIntroduce());
            collectDTOS.add(collectDTO);
        }
        return BaseResult.success(collectDTOS);
    }
}
