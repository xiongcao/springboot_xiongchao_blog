package com.xiongchao.blog.dao;


import com.xiongchao.blog.bean.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByUserIdAndStatus(Integer userId, Integer status);


    List<Comment> findAllByUserId(Integer userId);

//    /**
//      * 查询所有评论所属博主和博客
//      * @param userId
//      * @return
//      */
//    @Query(value = "SELECT c.*, u.name, u.remark, u.nickname, e.title FROM `comment` c  LEFT JOIN `user` u ON c.to_user_id = u.id LEFT JOIN essay e ON c.essay_id = e.id WHERE c.user_id = ?", nativeQuery = true)
//    List<Comment> findAllByUserId(Integer userId);


    /**
     * 根据文章id查询评论数量
     * @param essayId
     * @return
     */
    @Query(value = "SELECT COUNT(*) commentNumber FROM `comment` c WHERE c.essay_id = ? AND c.status = 1", nativeQuery = true)
    Integer findNumberByEssayId(Integer essayId);

    @Query(value = "SELECT * FROM `comment` c WHERE c.essay_id = ? AND c.status = 1 ORDER BY c.created_date DESC", nativeQuery = true)
    List<Comment> findByEssayId(Integer essayId);
}
