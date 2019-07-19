package com.xiongchao.blog.controller;

import com.xiongchao.blog.DTO.EssayDTO;
import com.xiongchao.blog.bean.BaseResult;
import com.xiongchao.blog.bean.Constants;
import com.xiongchao.blog.bean.Essay;
import com.xiongchao.blog.service.EssayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(description = "文章管理")
@RestController
@RequestMapping("essay")
public class EssayController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Logger loggerUser = LoggerFactory.getLogger("ADMIN");

    @Autowired
    private EssayService essayService;

    @PostMapping("save")
    @ApiOperation("保存文章")
    public BaseResult save(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                           @Valid @RequestBody EssayDTO essayDTO) {
        if(essayDTO.getId() != null){
            Essay essay = essayService.findByIdAndUserId(essayDTO.getId(), adminId);
            if (null == essay) {
                return BaseResult.failure("该文章不存在或非本人文章");
            }
        }
        essayDTO.setUserId(adminId);
        essayService.save(essayDTO);
        return BaseResult.success();
    }

    @PostMapping("updateStatus/{id}/{status}")
    @ApiOperation("修改状态")
    public BaseResult updateStatus(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                                   @ApiParam("ID") @PathVariable("id") Integer id,
                                   @ApiParam("0:删除;1:正常;") @PathVariable("status") Integer status) {
        Essay essay = essayService.findByIdAndUserId(id, adminId);
        if (null == essay) {
            return BaseResult.failure("该文章不存在或非本人文章");
        }
        essay.setStatus(status);
        essayService.save(essay);
        return BaseResult.success();
    }

    @GetMapping("findAll")
    @ApiOperation("查询所有文章")
    public BaseResult findAll(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                                   @ApiParam("0:删除;1:正常;") @RequestParam(value = "status", required = false) Integer status) {
        return BaseResult.success(essayService.findAllByUserIdAndStatus(adminId, status));
    }
}