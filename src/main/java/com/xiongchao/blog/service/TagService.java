package com.xiongchao.blog.service;

import com.alibaba.fastjson.JSONObject;
import com.xiongchao.blog.bean.Tag;
import com.xiongchao.blog.dao.EssayTagMappingRepository;
import com.xiongchao.blog.dao.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;


@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private EssayTagMappingRepository essayTagMappingRepository;

    @Autowired
    private EntityManager em;

    public Tag save(Tag tag){
        return tagRepository.save(tag);
    }

    public List<Tag> saveAll(List<Tag> tags) {
        return tagRepository.saveAll(tags);
    }

    public List<Tag> findAllByUserIdAndStatus(Integer userId, Integer status){
        if(status == null){
            return tagRepository.findAllByUserIdAndStatus(userId);
        }
        return tagRepository.findAllByUserIdAndStatus(userId, status);
    }

    @Cacheable(value = "findTagById", key = "#id")
    public Optional<Tag> findById(Integer id){
        return tagRepository.findById(id);
    }

    public List<Tag> findByNameAndUserId(String name, Integer userId){
        return tagRepository.findByNameAndUserId(name, userId);
    }

    public Tag findByIdAndUserId(Integer id, Integer userId){
        return tagRepository.findByIdAndUserId(id, userId);
    }

    public List<Tag> findListByEssayId(Integer essayId) {
        return tagRepository.findListByEssayId(essayId);
    }

    @Transactional
    public Integer deleteByEssayId(Integer essayId){
        return essayTagMappingRepository.deleteByEssayId(essayId);
    }

    @Transactional
    public void deleteById(Integer id){
        tagRepository.deleteById(id);
    }

    /**
     * 获取所有文章中标签使用次数最多的标签
     */
    public List<Tag> findTagNumber(Integer userId){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT m.tag_id id FROM ( SELECT et.tag_id, COUNT(et.tag_id) tagNum FROM essay e LEFT JOIN essay_tag_mapping et ON e.id = et.essay_id WHERE e.STATUS = 1");
        if (userId != null) {
            sb.append(" AND e.user_id = " + userId);
        }
        sb.append(" GROUP BY et.tag_id ORDER BY tagNum DESC ) m");
        Query query = em.createNativeQuery(sb.toString());
        List<Tag> tags = new ArrayList<>();
        List<Object> objects = query.getResultList();
        for (int i = 0; i < objects.size(); i++) {
            Object id = objects.get(i);
            if (id != null) {
                Tag tag = findById(Integer.parseInt(id.toString())).orElseThrow(()-> new RuntimeException("没有此标签"));
                tags.add(tag);
            }
        }
        return tags;
    }

    /**
     * 将用户所有文章中的标签进行分组，查出对应标签的文章数量
     * @param userId
     * @return
     */
    public List findTagEssayNumByUserId(Integer userId){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT et.tag_id id, t.name, COUNT(et.tag_id) essayNum FROM essay e LEFT JOIN essay_tag_mapping et ON e.id = et.essay_id LEFT JOIN tag t ON t.id = et.tag_id WHERE e.status = 1 AND e.user_id = "+userId+" GROUP BY et.tag_id");
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
