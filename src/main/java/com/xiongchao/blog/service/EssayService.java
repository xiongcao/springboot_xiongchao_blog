package com.xiongchao.blog.service;

import com.alibaba.fastjson.JSON;
import com.xiongchao.blog.DTO.EssayDTO;
import com.xiongchao.blog.bean.*;
import com.xiongchao.blog.dao.EssayCategoryMappingRepository;
import com.xiongchao.blog.dao.EssayRepository;
import com.xiongchao.blog.dao.EssayTagMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class EssayService {

    @Autowired
    private EssayRepository essayRepository;

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

    public List<Essay> findAllByUserIdAndStatus(Integer userId, Integer status){
        if(status == null){
            return essayRepository.findAllByUserId(userId);
        }
        return essayRepository.findAllByUserIdAndStatus(userId, status);
    }

    public Optional<Essay> findById(Integer id){
        return essayRepository.findById(id);
    }

    public  Essay findByIdAndUserId(Integer id, Integer userId){
        return essayRepository.findByIdAndUserId(id, userId);
    }

}
