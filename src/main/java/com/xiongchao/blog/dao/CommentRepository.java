package com.xiongchao.blog.dao;


import com.xiongchao.blog.bean.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByUserIdAndStatus(Integer userId, Integer status);

    List<Comment> findAllByUserId(Integer userId);
}
