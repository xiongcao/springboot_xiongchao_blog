package com.xiongchao.blog.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiongchao.blog.DTO.CommentDTO;
import com.xiongchao.blog.bean.*;
import com.xiongchao.blog.service.CommentService;
import com.xiongchao.blog.service.EssayService;
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

@Api(description = "评论管理")
@RestController
@RequestMapping("comment")
public class CommentController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Logger loggerUser = LoggerFactory.getLogger("ADMIN");

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private EssayService essayService;

    @PostMapping("save")
    @ApiOperation("保存评论")
    public BaseResult save(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                           @Valid @RequestBody Comment comment) {
        if(comment.getId() != null){
            commentService.findById(comment.getId()).orElseThrow(()-> new RuntimeException("没有此评论"));
        }
        comment.setUserId(adminId);
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
        comment.setUserId(adminId);
        commentService.save(comment);
        return BaseResult.success();
    }

    @GetMapping("findAll")
    @ApiOperation("查询所有评论")
    public BaseResult findAll(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId) {
        List<Comment> comments = commentService.findAllByUserId(adminId);
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment: comments) {
            CommentDTO commentDTO = JSONObject.parseObject(JSON.toJSONString(comment), CommentDTO.class);
            // 查询 被评论 博主
            User user = userService.findById(comment.getToUserId()).orElseThrow(() ->  new RuntimeException("用户不存在"));
            // 查询 被评论博客
            Essay essay = essayService.findById(comment.getEssayId()).orElseThrow(() ->  new RuntimeException("查无此博客"));
            commentDTO.setName(user.getName());
            commentDTO.setNickname(user.getNickname());
            commentDTO.setRemark(user.getIntroduce());
            commentDTO.setTitle(essay.getTitle());
            commentDTOS.add(commentDTO);
        }
        return BaseResult.success(commentDTOS);
    }
}
