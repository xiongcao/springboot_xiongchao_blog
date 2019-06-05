package com.xiongchao.blog.service;

import com.xiongchao.blog.bean.User;
import com.xiongchao.blog.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByUsername (String name) {
        return userRepository.findByName(name);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public  User findByPhoneNumber(String phone) {
        return userRepository.findByPhoneNumber(phone);
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }



}
