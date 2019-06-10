package com.xiongchao.blog.dao;


import com.xiongchao.blog.bean.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Integer> {

    @Query(value = "select f.* from Follow f where f.user_id = ?1", nativeQuery = true)
    List<Follow> findByUserId(Integer userId);

    Follow findByUserIdAndFollowUserId(Integer userId, Integer followUserId);
}
