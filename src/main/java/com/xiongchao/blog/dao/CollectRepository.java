package com.xiongchao.blog.dao;


import com.xiongchao.blog.bean.Collect;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CollectRepository extends JpaRepository<Collect, Integer> {
    List<Collect> findAllByUserIdAndStatus(Integer userId, Integer status);

    Collect findByIdAndUserId(Integer id, Integer userId);

    List<Collect> findAllByUserId(Integer userId);
}
