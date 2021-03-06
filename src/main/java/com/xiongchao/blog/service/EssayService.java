package com.xiongchao.blog.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiongchao.blog.DTO.CommentDTO;
import com.xiongchao.blog.DTO.EssayDTO;
import com.xiongchao.blog.bean.*;
import com.xiongchao.blog.dao.*;
import com.xiongchao.blog.util.SqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;


@Service
public class EssayService {

    @Autowired
    private EssayRepository essayRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EssayTagMappingRepository essayTagMappingRepository;

    @Autowired
    private EssayCategoryMappingRepository essayCategoryMappingRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CollectService collectService;

    @Autowired
    private StarService starService;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager em;

    public Essay save(Essay essay) {
        return essayRepository.save(essay);
    }

    public void save(EssayDTO essayDTO) {
        List<Tag> tags = essayDTO.getTags();
        List<Category> categorys = essayDTO.getCategorys();
        Essay essay = JSON.parseObject(JSON.toJSONString(essayDTO), Essay.class);
        essay = essayRepository.save(essay);
        // 保存标签
        List<EssayTagMapping> essayTagMappings = new ArrayList<>();
        for (Tag tag : tags) {
            essayTagMappings.add(new EssayTagMapping(essay.getId(), tag.getId()));
        }
        essayTagMappingRepository.saveAll(essayTagMappings);

        // 保存类型
        List<EssayCategoryMapping> essayCategoryMappings = new ArrayList<>();
        for (Category category : categorys) {
            essayCategoryMappings.add(new EssayCategoryMapping(essay.getId(), category.getId()));
        }
        essayCategoryMappingRepository.saveAll(essayCategoryMappings);
    }

    public List<EssayDTO> findAllByUserIdAndStatus(Integer userId, Integer status) {
        List<Essay> essays;
        if (status == null) {
            essays = essayRepository.findAllByUserIdNotDelete(userId);
        } else {
            essays = essayRepository.findByUserIdAndStatusOrderByRankDesc(userId, status);
        }
        return margeEssay(essays);
    }

    /**
     * 根据搜索条件分页查询所有人所有文章
     *
     * @param title
     * @param categoryId
     * @param tagId
     * @param basePage
     * @return
     */
    public Page<EssayDTO> findAllPage(String title, Integer categoryId, Integer tagId, Integer adminId, Integer status, BasePage basePage) {
        Integer page = null == basePage.getPage() ? 0 : basePage.getPage();
        Integer size = null == basePage.getSize() ? 20 : basePage.getSize();
        String direction = null == basePage.getDirection() ? "DESC" : basePage.getDirection();
        String[] properties;
        if (null == basePage.getProperties()) {
            properties = new String[1];
            properties[0] = "created_date";
        } else {
            properties = basePage.getProperties();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT " + SqlUtil.sqlGenerate("e", Essay.class) + " FROM essay e");
        sb.append(" LEFT JOIN essay_category_mapping ec ON e.id = ec.essay_id");
        sb.append(" LEFT JOIN essay_tag_mapping et ON e.id = et.essay_id");
        if (status != null) {
            sb.append(" WHERE e.status = " + status);
        } else {
            sb.append(" WHERE 1 = 1");
        }
        if (adminId != null) {
            sb.append(" AND e.user_id = " + adminId);
        }
        if (categoryId != null) {
            sb.append(" AND ec.category_id = " + categoryId);
        }
        if (tagId != null) {
            sb.append(" AND et.tag_id = " + tagId);
        }
        if (!StringUtils.isEmpty(title)) {
            sb.append(" AND e.title LIKE '%" + title + "%'");
        }
        sb.append(" GROUP BY e.id");
        sb.append(" ORDER BY");
        for (int i = 0; i < properties.length; i++) {
            if (i == properties.length - 1) {
                sb.append(" e." + properties[i] + " " + direction);
            } else {
                sb.append(" e." + properties[i] + " " + direction + ",");
            }
        }
        Query query = em.createNativeQuery(sb.toString());
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        List<Essay> essays = new ArrayList<>();
        List<Object> objects = query.getResultList();
        for (Object o : objects) {
            Object[] obj = (Object[]) o;
            essays.add(SqlUtil.toBean(obj, Essay.class));
        }
        if (essays.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);
        }
        List<EssayDTO> essayDTOS = margeEssay(essays);
        return new PageImpl<>(essayDTOS, PageRequest.of(page, size), findListCount(title, categoryId, tagId, adminId, status));
    }

    public List<EssayDTO> margeEssay(List<Essay> essays) {
        List<EssayDTO> essayDTOS = new ArrayList<>();
        for (Essay essay : essays) {
            EssayDTO essayDTO = JSONObject.parseObject(JSON.toJSONString(essay), EssayDTO.class);
            essayDTO.setTags(tagRepository.findListByEssayId(essay.getId()));
            essayDTO.setCategorys(categoryRepository.findListByEssayId(essay.getId()));
            essayDTO.setCommentNumber(commentService.findNumberByEssayId(essay.getId()));
            essayDTOS.add(essayDTO);
        }
        return essayDTOS;
    }

    @Cacheable(value = "findEssayById", key = "#id")
    public Optional<Essay> findById(Integer id) {
        return essayRepository.findById(id);
    }

    public EssayDTO findEssayJoinCommentById(Integer id, Integer adminId, Integer status) {
        Integer userId = 2;
        if (adminId != null) {
            userId = adminId;
        }
        Essay essay = new Essay();
        if (id != null) {
            Optional<Essay> essay0 = findById(id);
            essay = JSONObject.parseObject(JSON.toJSONString(essay0), Essay.class);
        } else {
            essay = essayRepository.findByUserIdAndStatus(userId, status);
        }
        if (essay == null) {
            return null;
        }
        // 添加浏览记录
        essay.setBrowseNumber(essay.getBrowseNumber() + 1);
        essayRepository.save(essay);
        EssayDTO essayDTO = JSONObject.parseObject(JSON.toJSONString(essay), EssayDTO.class);
        // 查询类型
        essayDTO.setCategorys(categoryRepository.findListByEssayId(essay.getId()));
        // 查询标签
        essayDTO.setTags(tagRepository.findListByEssayId(essay.getId()));
        // 查询评论
        List<CommentDTO> commentDTOS = new ArrayList<>();
        List<Comment> comments = commentService.findByEssayId(essay.getId());
        for (Comment comment: comments) {
            CommentDTO commentDTO = JSONObject.parseObject(JSON.toJSONString(comment), CommentDTO.class);
            // 查询评论者
            User user = userService.findById(comment.getUserId()).orElseThrow(() ->  new RuntimeException("用户不存在"));
            // 查询被评论者
            User toUser = userService.findById(comment.getToUserId()).orElseThrow(() ->  new RuntimeException("用户不存在"));
            // 查询评论是否点赞
            Star star = starService.findByCommentIdAndUserId(comment.getId(), adminId);
            if (star != null) {
                commentDTO.setCommentLikeStatus(star.getCommentLikeStatus());
            } else {
                commentDTO.setCommentLikeStatus(-1);
            }
            commentDTO.setUser(user);
            commentDTO.setToUser(toUser);
            commentDTOS.add(commentDTO);
        }
        essayDTO.setCommentDTOS(commentDTOS);
        // 是否已被收藏
        Collect collect = null;
        // 是否已点赞
        Star star = null;
        // 查询是否被转发
        Boolean forward = false;
        if (adminId != null && adminId != 0) {
            collect = collectService.findByEssayIdAndUserId(essay.getId(), adminId);
            star = starService.findByEssayIdAndUserId(essay.getId(), adminId);
            Essay essay1 = essayRepository.findByUserIdAndPid(adminId, essay.getId());
            if (essay1 != null) {
                forward = true;
            }
        }
        essayDTO.setForward(forward);
        essayDTO.setCollect(collect);
        essayDTO.setStar(star);
        return essayDTO;
    }

    public EssayDTO findByIdAndUserId(Integer id) {
        Optional<Essay> essay = findById(id);
        EssayDTO essayDTO = JSONObject.parseObject(JSON.toJSONString(essay), EssayDTO.class);
        List<Tag> tags = tagRepository.findListByEssayId(essayDTO.getId());
        List<Category> categorys = categoryRepository.findListByEssayId(essayDTO.getId());
        essayDTO.setTags(tags);
        essayDTO.setCategorys(categorys);
        return essayDTO;
    }

    public Long findListCount(String title, Integer categoryId, Integer tagId, Integer userId, Integer status) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(*) FROM essay e");
        sb.append(" LEFT JOIN essay_category_mapping ec ON e.id = ec.essay_id");
        sb.append(" LEFT JOIN essay_tag_mapping et ON e.id = et.essay_id");
        if (status != null) {
            sb.append(" WHERE e.status = " + status);
        } else {
            sb.append(" WHERE e.status = 1");
        }
        if (userId != null) {
            sb.append(" AND e.user_id = " + userId);
        }
        if (categoryId != null) {
            sb.append(" AND ec.category_id = " + categoryId);
        }
        if (tagId != null) {
            sb.append(" AND et.tag_id = " + tagId);
        }
        if (title != null) {
            sb.append(" AND e.title LIKE '%" + title + "%'");
        }
        Query query = em.createNativeQuery(sb.toString());
        List<Object> objects = query.getResultList();
        return Long.parseLong(objects.get(0).toString());
    }

    public Essay findPreEssay(Integer id, Integer userId) {
        return essayRepository.findPreEssay(id, userId);
    }

    public Essay findNextEssay(Integer id, Integer userId) {
        return essayRepository.findNextEssay(id, userId);
    }

    public Essay findByUserIdAndPid(Integer userId, Integer essayId) {
        return essayRepository.findByUserIdAndPid(userId, essayId);
    }
}
