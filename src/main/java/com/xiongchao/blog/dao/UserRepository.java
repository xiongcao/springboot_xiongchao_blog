package com.xiongchao.blog.dao;

import com.xiongchao.blog.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String name);

    User findByPhoneNumber(String phone);
}
