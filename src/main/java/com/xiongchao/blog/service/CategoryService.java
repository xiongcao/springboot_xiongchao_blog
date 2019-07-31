package com.xiongchao.blog.service;

import com.xiongchao.blog.bean.Category;
import com.xiongchao.blog.dao.CategoryRepository;
import com.xiongchao.blog.dao.EssayCategoryMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EssayCategoryMappingRepository essayCategoryMappingRepository;

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> saveAll(List<Category> categories) {
        return categoryRepository.saveAll(categories);
    }

    public List<Category> findAllByUserIdAndStatus(Integer userId, Integer status) {
        if (status == null) {
            return categoryRepository.findAllByUserIdAndStatus(userId);
        }
        return categoryRepository.findAllByUserIdAndStatus(userId, status);
    }

    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }

    public Category findByIdAndUserId(Integer id, Integer userId) {
        return categoryRepository.findByIdAndUserId(id, userId);
    }

    @Transactional
    public Integer deleteByEssayId(Integer essayId) {
        return essayCategoryMappingRepository.deleteByEssayId(essayId);
    }

    @Transactional
    public void deleteById(Integer id){
        categoryRepository.deleteById(id);
    }

}
