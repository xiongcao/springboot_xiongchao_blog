package com.xiongchao.blog.dao;


import com.xiongchao.blog.bean.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Integer> {

    /**
     * 根据状态查询所有类型
     * @param userId
     * @param status
     * @return
     */
    @Query(value = "select * from category c WHERE c.user_id = ?1 and c.status = ?2 ORDER BY c.rank DESC", nativeQuery = true)
    List<Category> findAllByUserIdAndStatus(Integer userId, Integer status);

    Category findByIdAndUserId(Integer id, Integer userId);

    /**
     * 查询所有类型
     * @param userId
     * @return
     */
    List<Category> findAllByUserId(Integer userId);

    /**
     * 查询所有未删除且根据rank降序排序的类型
     * @param userId
     * @return
     */
    @Query(value = "select * from category c WHERE c.user_id = ? and c.status <> -1 ORDER BY c.rank DESC", nativeQuery = true)
    List<Category> findAllByUserIdAndStatus(Integer userId);
}
