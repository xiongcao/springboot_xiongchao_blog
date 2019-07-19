package com.xiongchao.blog.controller;

import com.xiongchao.blog.bean.BaseResult;
import com.xiongchao.blog.bean.Category;
import com.xiongchao.blog.bean.Constants;
import com.xiongchao.blog.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(description = "类型管理")
@RestController
@RequestMapping("category")
public class CategoryController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Logger loggerUser = LoggerFactory.getLogger("ADMIN");

    @Autowired
    private CategoryService categoryService;

    @PostMapping("save")
    @ApiOperation("保存类型")
    public BaseResult save(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                           @ApiIgnore @SessionAttribute(Constants.ROLE) String role,
                           @Valid @RequestBody Category category) {
        if (role.equals("ROLE_SUPER")) {
            return BaseResult.failure(1, "没有权限");
        }
        category.setUserId(adminId);
        if(category.getId() != null){
            Category category1 = categoryService.findByIdAndUserId(category.getId(), adminId);
            if (null == category1) {
                return BaseResult.failure("该类型不存在或非本人类型");
            }
        }
        categoryService.save(category);
        return BaseResult.success();
    }

    @PostMapping("updateStatus/{id}/{status}")
    @ApiOperation("修改状态")
    public BaseResult updateStatus(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                                   @ApiIgnore @SessionAttribute(Constants.ROLE) String role,
                                   @ApiParam("ID") @PathVariable("id") Integer id,
                                   @ApiParam("-1:删除;0:隐藏;1:正常;") @PathVariable("status") Integer status) {
        if (role.equals("ROLE_SUPER")) {
            return BaseResult.failure(1, "没有权限");
        }
        Category category = categoryService.findById(id).orElseThrow(()-> new RuntimeException("没有此类型"));
        category.setStatus(status);
        categoryService.save(category);
        return BaseResult.success();
    }

    @GetMapping("findAll")
    @ApiOperation("查询所有类型")
    public BaseResult findAll(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                                   @ApiParam("0:删除;1:正常;") @RequestParam(value = "status", required = false) Integer status) {
        return BaseResult.success(categoryService.findAllByUserIdAndStatus(adminId, status));
    }
}
