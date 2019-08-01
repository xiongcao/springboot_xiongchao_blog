package com.xiongchao.blog.service;

import com.xiongchao.blog.bean.Tag;
import com.xiongchao.blog.dao.EssayTagMappingRepository;
import com.xiongchao.blog.dao.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private EssayTagMappingRepository essayTagMappingRepository;

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

}
