package com.xiongchao.blog.service;

import com.xiongchao.blog.bean.Category;
import com.xiongchao.blog.dao.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category save(Category category){
        return categoryRepository.save(category);
    }

    public List<Category> findAllByUserIdAndStatus(Integer userId, Integer status){
        if(status == null){
            return categoryRepository.findAllByUserIdAndStatus(userId);
        }
        return categoryRepository.findAllByUserIdAndStatus(userId, status);
    }

    public Optional<Category> findById(Integer id){
        return categoryRepository.findById(id);
    }

    public Category findByIdAndUserId(Integer id, Integer userId){
        return categoryRepository.findByIdAndUserId(id, userId);
    }

}
