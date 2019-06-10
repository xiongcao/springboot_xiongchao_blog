package com.xiongchao.blog.dao;


import com.xiongchao.blog.bean.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findAllByUserIdAndStatus(Integer userId, Integer status);

    Category findByIdAndUserId(Integer id, Integer userId);

    List<Category> findAllByUserId(Integer userId);
}
