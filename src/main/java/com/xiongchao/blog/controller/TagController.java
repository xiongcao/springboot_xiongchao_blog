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
                           @Valid @RequestBody Tag tag) {
        tag.setUserId(adminId);
        if(tag.getId() != null){
            Tag tag1 = tagService.findByIdAndUserId(tag.getId(), adminId);
            if (tag1 == null) {
                return BaseResult.failure("该标签不存在或非本人标签");
            }
        } else {
            List<Tag> tags = tagService.findByNameAndUserId(tag.getName(), tag.getUserId());
            if (tags.size() != 0) {
                return BaseResult.failure("已存在该标签");
            }
        }
        tagService.save(tag);
        return BaseResult.success("保存成功");
    }

    @PostMapping("saveAll")
    @ApiOperation("批量添加标签")
    public BaseResult save(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                           @Valid @RequestBody List<Tag> tags) {
        for (Tag tag: tags) {
            tag.setUserId(adminId);
            List<Tag> tags1 = tagService.findByNameAndUserId(tag.getName(), tag.getUserId());
            if (tags1.size() != 0) {
                tagService.deleteById(tags1.get(0).getId());
            }
        }
        tagService.saveAll(tags);
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
        return BaseResult.success(tagService.findAllByUserIdAndStatus(1, status));
    }

    @GetMapping("findSuperAll")
    @ApiOperation("查询超管所有标签")
    public BaseResult findAll(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId) {
        return BaseResult.success(tagService.findAllByUserIdAndStatus(1, 1));
    }

    @GetMapping("findTagNumbers")
    @ApiOperation("获取所有文章中标签使用次数最多的前几个标签")
    public BaseResult findTagNumberList(@ApiParam("用户ID用户ID,传id则查改用户文章的标签，不传则查所有用户文章的标签") @RequestParam(value = "userId", required = false) Integer userId){
        List<Tag> tags = tagService.findTagNumber(userId);
        if (tags.size() > 10) {
            tags.subList(0, 10);
        }
        return BaseResult.success(tags);
    }

    @GetMapping("findTagEssayNumber")
    @ApiOperation("查询用户所有文章中的使用到的标签的文章数量")
    public BaseResult findTagEssayNumber(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId){
        return BaseResult.success(tagService.findTagEssayNumByUserId(adminId));
    }
}
