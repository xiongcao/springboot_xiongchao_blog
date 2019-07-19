package com.xiongchao.blog.controller;

import com.xiongchao.blog.bean.BaseResult;
import com.xiongchao.blog.bean.Constants;
import com.xiongchao.blog.bean.Tag;
import com.xiongchao.blog.service.TagService;
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

@Api(description = "标签管理")
@RestController
@RequestMapping("tag")
public class TagController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Logger loggerUser = LoggerFactory.getLogger("ADMIN");

    @Autowired
    private TagService tagService;

    @PostMapping("save")
    @ApiOperation("保存标签")
    public BaseResult save(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                           @ApiIgnore @SessionAttribute(Constants.ROLE) String role,
                           @Valid @RequestBody Tag tag) {
        if (role.equals("ROLE_SUPER")) {
            return BaseResult.failure(1, "没有权限");
        }
        tag.setUserId(adminId);
        if(tag.getId() != null){
            tagService.findById(tag.getId()).orElseThrow(()-> new RuntimeException("没有此标签"));
        }
        tagService.save(tag);
        return BaseResult.success("保存成功");
    }

    @PostMapping("updateStatus/{id}/{status}")
    @ApiOperation("修改状态")
    public BaseResult updateStatus(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                                   @ApiParam("ID") @PathVariable("id") Integer id,
                                   @ApiParam("-1:删除;0:隐藏;1:正常;") @PathVariable("status") Integer status) {
        Tag tag = tagService.findById(id).orElseThrow(()-> new RuntimeException("没有此标签"));
        tag.setStatus(status);
        tagService.save(tag);
        return BaseResult.success();
    }

    @GetMapping("findAll")
    @ApiOperation("查询所有标签")
    public BaseResult findAll(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                                   @ApiParam("0:删除;1:正常;") @RequestParam(value = "status", required = false) Integer status) {
        return BaseResult.success(tagService.findAllByUserIdAndStatus(adminId, status));
    }
}
