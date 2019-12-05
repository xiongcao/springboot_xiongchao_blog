package com.xiongchao.blog.service;

import com.alibaba.fastjson.JSONObject;
import com.xiongchao.blog.bean.Category;
import com.xiongchao.blog.bean.Essay;
import com.xiongchao.blog.dao.CategoryRepository;
import com.xiongchao.blog.dao.EssayCategoryMappingRepository;
import com.xiongchao.blog.util.SqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EssayCategoryMappingRepository essayCategoryMappingRepository;

    @Autowired
    private EntityManager em;

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> saveAll(List<Category> categories) {
        return categoryRepository.saveAll(categories);
    }

    public List<Category> findAll(Integer userId, Integer status, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT " + SqlUtil.sqlGenerate("c", Category.class) + " FROM category c WHERE c.user_id = " + userId);
        if (!StringUtils.isEmpty(name)) {
            sb.append(" AND c.`name` LIKE '%" + name + "%'" );
        }
        if (status != null) {
            sb.append(" AND c.`status` = " + status);
        }
        sb.append(" ORDER BY c.`rank` DESC, c.id DESC");
        Query query = em.createNativeQuery(sb.toString());
        List<Category> categories = new ArrayList<>();
        List<Object> objects = query.getResultList();
        for (Object o : objects) {
            Object[] obj = (Object[]) o;
            categories.add(SqlUtil.toBean(obj, Category.class));
        }
        return categories;
    }

    @Cacheable(value = "findCategoryById", key = "#id")
    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }

    public List<Category> findByNameAndUserId(String name, Integer userId){
        return categoryRepository.findByNameAndUserId(name, userId);
    }

    public Category findByIdAndUserId(Integer id, Integer userId) {
        return categoryRepository.findByIdAndUserId(id, userId);
    }

    public List<Category> findListByEssayId(Integer essayId) {
        return categoryRepository.findListByEssayId(essayId);
    }

    @Transactional
    public Integer deleteByEssayId(Integer essayId) {
        return essayCategoryMappingRepository.deleteByEssayId(essayId);
    }

    @Transactional
    public void deleteById(Integer id){
        categoryRepository.deleteById(id);
    }

    /**
     * 获取所有文章中标签使用次数最多的前几个类型
     */
    public List<Category> findCategoryNumber(Integer userId){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT m.category_id id FROM ( SELECT ec.category_id, COUNT(ec.category_id) tagNum FROM essay e LEFT JOIN essay_category_mapping ec ON e.id = ec.essay_id WHERE e.STATUS = 1");
        if (userId != null) {
            sb.append(" AND e.user_id = " + userId);
        }
        sb.append(" GROUP BY ec.category_id ORDER BY tagNum DESC ) m");
        Query query = em.createNativeQuery(sb.toString());
        List<Category> categories = new ArrayList<>();
        List<Object> objects = query.getResultList();
        for (int i = 0; i < objects.size(); i++) {
            Object id = objects.get(i);
            if (id != null) {
                Category category = findById(Integer.parseInt(id.toString())).orElseThrow(()-> new RuntimeException("没有此类型"));
                categories.add(category);
            }
        }
        return categories;
    }

    /**
     * 将用户所有文章中的类型进行分组，查出对应类型的文章数量
     * @param userId
     * @return
     */
    public List findCatetoryEssayNumByUserId(Integer userId){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ec.category_id id, c.name, COUNT(ec.category_id) essayNum FROM essay e LEFT JOIN essay_category_mapping ec ON e.id = ec.essay_id LEFT JOIN category c ON c.id = ec.category_id WHERE e.status = 1 AND e.user_id = "+userId+" GROUP BY ec.category_id");
        Query query = em.createNativeQuery(sb.toString());
        List<Object> objects = query.getResultList();
        List list = new ArrayList();
        for (int i = 0; i < objects.size(); i++) {
            Object[] obj = (Object[]) objects.get(i);
            Integer id = Integer.parseInt(obj[0].toString());
            String name = obj[1].toString();
            Integer num = Integer.parseInt(obj[2].toString());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("name", name);
            jsonObject.put("num", num);
            list.add(jsonObject);
        }
        return list;
    }

}
