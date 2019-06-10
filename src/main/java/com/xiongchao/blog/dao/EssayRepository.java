package com.xiongchao.blog.dao;


import com.xiongchao.blog.bean.Essay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface EssayRepository extends JpaRepository<Essay, Integer> {
    List<Essay> findAllByUserIdAndStatus(Integer userId, Integer status);

    List<Essay> findAllByUserId(Integer userId);

    Essay findByIdAndUserId(Integer id, Integer userId);
}
