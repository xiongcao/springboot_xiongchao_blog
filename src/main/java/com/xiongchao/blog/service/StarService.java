package com.xiongchao.blog.service;

import com.xiongchao.blog.bean.Star;
import com.xiongchao.blog.dao.StarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class StarService {

    @Autowired
    private StarRepository starRepository;

    public Star save(Star star) {
        return starRepository.save(star);
    }

    public List<Star> findAllByUserId(Integer userId) {
        return starRepository.findAllByUserId(userId);
    }

    public Optional<Star> findById(Integer id) {
        return starRepository.findById(id);
    }

    public Star findByIdAndUserId(Integer id, Integer userId) {
        return starRepository.findByIdAndUserId(id, userId);
    }

    public Star findByEssayIdAndUserId(Integer essayId, Integer userId) {
        return starRepository.findByEssayIdAndUserId(essayId, userId);
    }


}
