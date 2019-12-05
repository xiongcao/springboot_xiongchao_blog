package com.xiongchao.blog.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiongchao.blog.DTO.EssayDTO;
import com.xiongchao.blog.bean.*;
import com.xiongchao.blog.service.CategoryService;
import com.xiongchao.blog.service.EssayService;
import com.xiongchao.blog.service.TagService;
import com.xiongchao.blog.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Api(description = "文章管理")
@RestController
@RequestMapping("essay")
public class EssayController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Logger loggerUser = LoggerFactory.getLogger("ADMIN");

    @Autowired
    private EssayService essayService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @PostMapping("save")
    @ApiOperation("保存文章")
    public BaseResult save(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                           @Valid @RequestBody EssayDTO essayDTO) {
        if(essayDTO.getId() != null){
            Essay essay = essayService.findByIdAndUserId(essayDTO.getId());
            if (null == essay) {
                return BaseResult.failure("该文章不存在或非本人文章");
            }
            // 删除映射表中与之关联的所有标签、类型数据
            tagService.deleteByEssayId(essayDTO.getId());
            categoryService.deleteByEssayId(essayDTO.getId());
        }
        List<Tag> tags = essayDTO.getTags();
        if (tags == null || tags.isEmpty()) {
            return BaseResult.failure("缺少标签参数");
        }
        List<Category> categories = essayDTO.getCategorys();
        if (categories == null || categories.isEmpty()) {
            return BaseResult.failure("缺少类型参数");
        }
        essayDTO.setUserId(adminId);
        essayService.save(essayDTO);
        return BaseResult.success();
    }

    @PostMapping("updateStatus/{id}/{status}")
    @ApiOperation("修改状态")
    public BaseResult updateStatus(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                                   @ApiParam("ID") @PathVariable("id") Integer id,
                                   @ApiParam("1：公开  2：私密 3：草稿 4: 自序 0：删除") @PathVariable("status") Integer status) {
        Essay essay = essayService.findById(id).orElseThrow(()->new RuntimeException("无此文章"));
        if (null == essay) {
            return BaseResult.failure("该文章不存在或非本人文章");
        }
        essay.setStatus(status);
        essayService.save(essay);
        return BaseResult.success();
    }

    @GetMapping("admin/findAll")
    @ApiOperation("后台查询所有文章")
    public BaseResult findAllByAdminId(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                                       @RequestParam(value = "title", required = false) @ApiParam("文章标题,不传表示查所有") String title,
                                       @RequestParam(value = "categoryId", required = false) @ApiParam("类型,不传表示查所有") Integer categoryId,
                                       @RequestParam(value = "tagId", required = false) @ApiParam("标签,不传表示查所有") Integer tagId,
                                       @ApiParam("1公开 2：私密 3：草稿 0：删除;") @RequestParam(value = "status", required = false) Integer status,
                                        BasePage basePage) {
        return BaseResult.success(essayService.findAllPage(title, categoryId, tagId, adminId, status, basePage));
    }

    /**
     * status: 1公开 2：私密 3：草稿 0：删除;
     * @return
     */
    @GetMapping("findAll")
    @ApiOperation("前台查询博主所有文章")
    public BaseResult findAll(@RequestParam(value = "title", required = false) @ApiParam("文章标题,不传表示查所有") String title,
                              @RequestParam(value = "categoryId", required = false) @ApiParam("类型,不传表示查所有") Integer categoryId,
                              @RequestParam(value = "tagId", required = false) @ApiParam("标签,不传表示查所有") Integer tagId,
                              @RequestParam(value = "id", required = false) @ApiParam("用户ID，不传查博客开发者") Integer id,
                              BasePage basePage) {
        Integer userId = 2;
        if (id != null) {
            userId = id;
        }
        return BaseResult.success(essayService.findAllPage(title, categoryId, tagId, userId, 1, basePage));
    }

    /**
     * 需要做分页，按点赞数，时间排序
     * @return
     */
    @GetMapping("findAllAuthorList")
    @ApiOperation("前台查询所有博主文章")
    public BaseResult findAllPage(@RequestParam(value = "categoryId", required = false) @ApiParam("类型,不传表示查所有") Integer categoryId,
                                    @RequestParam(value = "tagId", required = false) @ApiParam("标签,不传表示查所有") Integer tagId,
                                    @RequestParam(value = "title", required = false) @ApiParam("文章标题,不传表示查所有") String title,
                                    BasePage basePage) {
        return BaseResult.success(essayService.findAllPage(title, categoryId, tagId, null, 1, basePage));
    }

    @GetMapping("admin/detail/{id}")
    @ApiOperation("后台根据文章id查询文章信息")
    public BaseResult detail(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                             @ApiParam("ID") @PathVariable("id") Integer id) {
        return BaseResult.success(essayService.findByIdAndUserId(id));
    }

    @GetMapping("detail")
    @ApiOperation("前台根据文章id查询文章信息")
    public BaseResult detail(@ApiParam("ID") @RequestParam(value = "id", required = false) Integer id, HttpSession session,
                             @ApiParam("状态 1：公开  2：私密 3：草稿 4: 自序 0：删除") @RequestParam(value = "status", required = false) Integer status) {
        Object session_adminId = session.getAttribute(Constants.ADMIN_ID);
        Integer adminId = null;
        if (!StringUtils.isEmpty(session_adminId)) {
            adminId = Integer.parseInt(session_adminId.toString());
        }
        EssayDTO essayDTO = essayService.findEssayJoinCommentById(id, adminId, status);
        if (essayDTO == null) {
            return BaseResult.failure("没有查到此文章");
        }
        User user = userService.findById(essayDTO.getUserId()).orElseThrow(()-> new RuntimeException("没有查到用户信息"));
        essayDTO.setUser(user);
        essayDTO.setPreEssay(essayService.findPreEssay(essayDTO.getId(), essayDTO.getUserId()));
        essayDTO.setNextEssay(essayService.findNextEssay(essayDTO.getId(), essayDTO.getUserId()));
        return BaseResult.success(essayDTO);
    }

    @PostMapping("forward/{id}")
    @ApiOperation("转发")
    public BaseResult forward(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                                   @ApiParam("ID") @PathVariable("id") Integer id) {
        Essay essay0 = essayService.findByUserIdAndPid(adminId, id);
        if (essay0 != null) {
            return BaseResult.failure("你已转发过此文章");
        }
        Essay essay = essayService.findById(id).orElseThrow(()->new RuntimeException("无此文章"));
        if (null == essay) {
            return BaseResult.failure("该文章不存在或非本人文章");
        }
        Essay essay1 = new Essay();
        essay1.setTitle(essay.getTitle());
        essay1.setContent(essay.getContent());
        essay1.setCover(essay.getCover());
        essay1.setStatus(essay.getStatus());
        essay1.setUserId(adminId);
        essay1.setType(1);
        essay1.setPid(essay.getId());
        essayService.save(essay);
        List<Tag> tags = tagService.findListByEssayId(essay.getId());
        List<Category> categories = categoryService.findListByEssayId(essay.getId());
        EssayDTO essayDTO = JSONObject.parseObject(JSON.toJSONString(essay1), EssayDTO.class);
        essayDTO.setTags(tags);
        essayDTO.setCategorys(categories);
        essayService.save(essayDTO);
        return BaseResult.success();
    }

}
