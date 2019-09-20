package com.xiongchao.blog.controller;


import com.xiongchao.blog.DTO.EssayDTO;
import com.xiongchao.blog.bean.BaseResult;
import com.xiongchao.blog.bean.Constants;
import com.xiongchao.blog.bean.Project;
import com.xiongchao.blog.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(description = "项目管理")
@RestController
@RequestMapping("project")
public class ProjectController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Logger loggerUser = LoggerFactory.getLogger("ADMIN");

    @Autowired
    private ProjectService projectService;

    @PostMapping("save")
    @ApiOperation("保存项目")
    public BaseResult save(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                           @Valid @RequestBody Project project) {
        project.setUserId(adminId);
        if(project.getId() != null){
            Project project1 = projectService.findById(project.getId()).orElseThrow(() -> new RuntimeException("没有此项目"));
            if (project1 == null) {
                return BaseResult.failure("该项目不存在");
            }
        }
        projectService.save(project);
        return BaseResult.success();
    }

    @PostMapping("updateStatus/{id}/{status}")
    @ApiOperation("修改状态")
    public BaseResult updateStatus(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                                   @ApiParam("ID") @PathVariable("id") Integer id,
                                   @ApiParam("-1:删除;0:隐藏;1:正常;") @PathVariable("status") Integer status) {
        Project project = projectService.findById(id).orElseThrow(() -> new RuntimeException("没有此项目"));
        project.setStatus(status);
        projectService.save(project);
        return BaseResult.success();
    }

    @GetMapping("admin/findAll")
    @ApiOperation("后台查询所有")
    public BaseResult findAllByAdmin(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId) {
        return BaseResult.success(projectService.findAll());
    }

    @GetMapping("findAll")
    @ApiOperation("查询所有")
    public BaseResult findAll() {
        return BaseResult.success(projectService.findAllByUserId(2));
    }
}
