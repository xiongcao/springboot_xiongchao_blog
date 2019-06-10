package com.xiongchao.blog.service;

import com.xiongchao.blog.bean.Comment;
import com.xiongchao.blog.dao.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment save(Comment comment){
        return commentRepository.save(comment);
    }

    public List<Comment> findAllByUserIdAndStatus(Integer userId, Integer status){
        if(status == null){
            return commentRepository.findAllByUserId(userId);
        }
        return commentRepository.findAllByUserIdAndStatus(userId, status);
    }

    public Optional<Comment> findById(Integer id){
        return commentRepository.findById(id);
    }

}
