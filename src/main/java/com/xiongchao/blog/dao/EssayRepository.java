package com.xiongchao.blog.dao;


import com.xiongchao.blog.DTO.EssayDTO;
import com.xiongchao.blog.bean.Essay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface EssayRepository extends JpaRepository<Essay, Integer> {

    /**
     * 根据状态查询文章
     * @param userId
     * @param status
     * @return
     */
    List<Essay> findByUserIdAndStatusOrderByRankDesc(Integer userId, Integer status);

    /**
     * 查询用户所有文章
     * @param userId
     * @return
     */
    List<Essay> findAllByUserId(Integer userId);


    /**
     * 查询用户所有未删除的文章， 并依照rank降序排序，时间倒叙排序
     * @param userId
     * @return
     */
    @Query(value = "select * from essay c WHERE c.user_id = ? and c.status <> 0 ORDER BY c.rank DESC", nativeQuery = true)
    List<Essay> findAllByUserIdNotDelete(Integer userId);


    Essay findByIdAndUserId(Integer id, Integer userId);
}
