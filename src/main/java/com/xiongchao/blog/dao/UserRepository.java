package com.xiongchao.blog.dao;

import com.xiongchao.blog.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String name);

    User findByPhoneNumber(String phone);

    /**
     *
     * @param userId 用户ID
     * @param status 1：关注 2： 粉丝
     * @return
     */
    @Query(value = "SELECT COUNT(*) FROM follow f WHERE f.user_id = ?1 AND f.`status` = ?2", nativeQuery = true)
    Integer findFollowNumberByStatus(Integer userId, Integer status);

}
