package com.xiongchao.blog.dao;


import com.xiongchao.blog.bean.Collect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CollectRepository extends JpaRepository<Collect, Integer> {
    List<Collect> findAllByUserIdAndStatus(Integer userId, Integer status);

    Collect findByIdAndUserId(Integer id, Integer userId);


    @Query(value = "SELECT * from collect c WHERE c.user_id = ? AND c.status = 1", nativeQuery = true)
    List<Collect> findAllByUserId(Integer userId);
}
