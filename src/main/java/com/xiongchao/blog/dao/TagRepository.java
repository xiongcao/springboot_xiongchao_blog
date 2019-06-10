package com.xiongchao.blog.dao;


import com.xiongchao.blog.bean.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<Tag> findAllByUserIdAndStatus(Integer userId, Integer status);

    List<Tag> findAllByUserId(Integer userId);
}
