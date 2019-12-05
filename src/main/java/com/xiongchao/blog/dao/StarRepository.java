package com.xiongchao.blog.dao;


import com.xiongchao.blog.bean.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface StarRepository extends JpaRepository<Star, Integer> {
    List<Star> findAllByUserIdAndStatus(Integer userId, Integer status);

    Star findByIdAndUserId(Integer id, Integer userId);


    @Query(value = "SELECT * from star c WHERE c.user_id = ? AND c.status = 1", nativeQuery = true)
    List<Star> findAllByUserId(Integer userId);

    Star findByEssayIdAndUserId(Integer essayId, Integer userId);

    Star findByCommentIdAndUserId(Integer commentId, Integer userId);
}
