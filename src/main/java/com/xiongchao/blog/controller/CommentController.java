package com.xiongchao.blog.controller;

import com.xiongchao.blog.bean.BaseResult;
import com.xiongchao.blog.bean.Comment;
import com.xiongchao.blog.bean.Constants;
import com.xiongchao.blog.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(description = "评论管理")
@RestController
@RequestMapping("comment")
public class CommentController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Logger loggerUser = LoggerFactory.getLogger("ADMIN");

    @Autowired
    private CommentService commentService;

    @PostMapping("save")
    @ApiOperation("保存评论")
    public BaseResult save(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                           @Valid @RequestBody Comment comment) {
        if(comment.getId() != null){
            commentService.findById(comment.getId()).orElseThrow(()-> new RuntimeException("没有此评论"));
        }
        commentService.save(comment);
        return BaseResult.success();
    }

    @PostMapping("updateStatus/{id}/{status}")
    @ApiOperation("修改状态")
    public BaseResult updateStatus(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                                   @ApiParam("ID") @PathVariable("id") Integer id,
                                   @ApiParam("0:删除;1:正常;") @PathVariable("status") Integer status) {
        Comment comment = commentService.findById(id).orElseThrow(()-> new RuntimeException("没有此评论"));
        comment.setStatus(status);
        commentService.save(comment);
        return BaseResult.success();
    }

    @GetMapping("findAll")
    @ApiOperation("查询所有评论")
    public BaseResult findAll(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                                   @ApiParam("0:删除;1:正常;") @RequestParam(value = "status", required = false) Integer status) {
        return BaseResult.success(commentService.findAllByUserIdAndStatus(adminId, status));
    }
}
