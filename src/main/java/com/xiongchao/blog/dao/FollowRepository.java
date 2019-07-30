package com.xiongchao.blog.dao;


import com.xiongchao.blog.bean.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Integer> {

    @Query(value = "select f.* from Follow f where f.user_id = ?1", nativeQuery = true)
    List<Follow> findByUserId(Integer userId);

    /**
     * 查询该用户是否已被关注
     * @param userId
     * @param followUserId
     * @param status 1: 关注 2：粉丝
     * @return
     */
    @Query(value = "SELECT * FROM follow f WHERE f.user_id = ?1 AND f.follow_user_id = ?2 AND f.status = ?3", nativeQuery = true)
    Follow findByUserIdAndFollowUserId(Integer userId, Integer followUserId, Integer status);
}
