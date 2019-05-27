package com.xiongchao.blog.service;

import com.xiongchao.blog.bean.User;
import com.xiongchao.blog.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByUsername (String name) {
        return userRepository.findByName(name);
    }

}
