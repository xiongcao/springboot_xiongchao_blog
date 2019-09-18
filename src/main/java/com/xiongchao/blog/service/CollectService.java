package com.xiongchao.blog.service;

import com.xiongchao.blog.bean.Collect;
import com.xiongchao.blog.dao.CollectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CollectService {

    @Autowired
    private CollectRepository collectRepository;

    public Collect save(Collect collect) {
        return collectRepository.save(collect);
    }

    public List<Collect> findAllByUserId(Integer userId) {
        return collectRepository.findAllByUserId(userId);
    }

    public Optional<Collect> findById(Integer id) {
        return collectRepository.findById(id);
    }

    public Collect findByIdAndUserId(Integer id, Integer userId) {
        return collectRepository.findByIdAndUserId(id, userId);
    }

    public Collect findByEssayIdAndUserId(Integer essayId, Integer userId) {
        return collectRepository.findByEssayIdAndUserId(essayId, userId);
    }


}
