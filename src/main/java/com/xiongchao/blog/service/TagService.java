package com.xiongchao.blog.service;

import com.xiongchao.blog.bean.Tag;
import com.xiongchao.blog.dao.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public Tag save(Tag tag){
        return tagRepository.save(tag);
    }

    public List<Tag> findAllByUserIdAndStatus(Integer userId, Integer status){
        if(status == null){
            return tagRepository.findAllByUserId(userId);
        }
        return tagRepository.findAllByUserIdAndStatus(userId, status);
    }

    public Optional<Tag> findById(Integer id){
        return tagRepository.findById(id);
    }

}
