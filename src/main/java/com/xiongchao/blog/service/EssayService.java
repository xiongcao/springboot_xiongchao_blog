package com.xiongchao.blog.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiongchao.blog.DTO.EssayDTO;
import com.xiongchao.blog.bean.*;
import com.xiongchao.blog.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Essay save(Essay essay){
        return essayRepository.save(essay);
    }

    public void save(EssayDTO essayDTO){
        Essay essay = JSON.parseObject(JSON.toJSONString(essayDTO), Essay.class);
        essay = essayRepository.save(essay);

        // 保存标签
        List<Tag> tags = essayDTO.getTags();
        if(tags != null && !tags.isEmpty()){
            List<EssayTagMapping> essayTagMappings = new ArrayList<>();
            for(Tag tag : tags) {
                essayTagMappings.add(new EssayTagMapping(essay.getId(), tag.getId()));
            }
            essayTagMappingRepository.saveAll(essayTagMappings);
        }

        // 保存类型
        List<Category> categories = essayDTO.getCategories();
        if(categories != null && !categories.isEmpty()){
            List<EssayCategoryMapping> essayCategoryMappings = new ArrayList<>();
            for(Category category : categories) {
                essayCategoryMappings.add(new EssayCategoryMapping(essay.getId(), category.getId()));
            }
            essayCategoryMappingRepository.saveAll(essayCategoryMappings);
        }
    }

    public List<EssayDTO> findAllByUserIdAndStatus(Integer userId, Integer status){
        List<EssayDTO> essayDTOS = new ArrayList<>();
        List<Essay> essays;
        if(status == null){
            essays = essayRepository.findAllByUserIdNotDelete(userId);
        } else {
            essays = essayRepository.findByUserIdAndStatusOrderByRankDesc(userId, status);
        }
        for (Essay essay: essays) {
            EssayDTO essayDTO = JSONObject.parseObject(JSON.toJSONString(essay), EssayDTO.class);
            essayDTO.setTags(tagRepository.findListByEssayId(essay.getId()));
            essayDTO.setCategories(categoryRepository.findListByEssayId(essay.getId()));
            essayDTOS.add(essayDTO);
        }
        return essayDTOS;
    }

    public Optional<Essay> findById(Integer id){
        return essayRepository.findById(id);
    }

    public  Essay findByIdAndUserId(Integer id, Integer userId){
        return essayRepository.findByIdAndUserId(id, userId);
    }

}
