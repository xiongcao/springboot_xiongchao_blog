package com.xiongchao.blog.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiongchao.blog.DTO.CollectDTO;
import com.xiongchao.blog.bean.*;
import com.xiongchao.blog.service.CollectService;
import com.xiongchao.blog.service.EssayService;
import com.xiongchao.blog.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Api(description = "收藏管理")
@RestController
@RequestMapping("collect")
public class CollectController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Logger loggerUser = LoggerFactory.getLogger("ADMIN");

    @Autowired
    private CollectService collectService;

    @Autowired
    private UserService userService;

    @Autowired
    private EssayService essayService;

    @PostMapping("save")
    @ApiOperation("保存")
    public BaseResult save(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                           @Valid @RequestBody Collect collect) {
        if(collect.getId() != null){
            Collect collect1 = collectService.findByIdAndUserId(collect.getId(), adminId);
            if (null == collect1) {
                return BaseResult.failure("该收藏不存在或非本人收藏");
            }
        }
        collect.setUserId(adminId);
        collectService.save(collect);
        return BaseResult.success();
    }

    @PostMapping("updateStatus/{id}/{status}")
    @ApiOperation("修改状态")
    public BaseResult updateStatus(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                                   @ApiParam("ID") @PathVariable("id") Integer id,
                                   @ApiParam("0:删除;1:正常;") @PathVariable("status") Integer status) {
        Collect collect = collectService.findById(id).orElseThrow(()-> new RuntimeException("没有找到此收藏"));
        collect.setStatus(status);
        collectService.save(collect);
        return BaseResult.success();
    }

    @GetMapping("findAll")
    @ApiOperation("查询所有收藏文章")
    public BaseResult findAll(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId) {
        List<CollectDTO> collectDTOS = new ArrayList<>();
        List<Collect> collects  = collectService.findAllByUserId(adminId);
        for (Collect collect: collects) {
            CollectDTO collectDTO = JSONObject.parseObject(JSON.toJSONString(collect), CollectDTO.class);
            Essay essay = essayService.findById(collect.getEssayId()).orElseThrow(() ->  new RuntimeException("文章不存在"));
            collectDTO.setTitle(essay.getTitle());
            User user = userService.findById(collect.getUserId()).orElseThrow(() ->  new RuntimeException("用户不存在"));
            collectDTO.setName(user.getName());
            collectDTO.setNickname(user.getNickname());
            collectDTO.setRemark(user.getIntroduce());
            collectDTOS.add(collectDTO);
        }
        return BaseResult.success(collectDTOS);
    }
}
