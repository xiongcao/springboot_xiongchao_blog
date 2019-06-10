package com.xiongchao.blog.controller;

import com.xiongchao.blog.bean.BaseResult;
import com.xiongchao.blog.bean.Collect;
import com.xiongchao.blog.bean.Constants;
import com.xiongchao.blog.service.CollectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(description = "收藏管理")
@RestController
@RequestMapping("collect")
public class CollectController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Logger loggerUser = LoggerFactory.getLogger("ADMIN");

    @Autowired
    private CollectService collectService;

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
    public BaseResult findAll(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                                   @ApiParam("0:删除;1:正常;") @RequestParam(value = "status", required = false) Integer status) {
        return BaseResult.success(collectService.findAllByUserIdAndStatus(adminId, status));
    }
}
