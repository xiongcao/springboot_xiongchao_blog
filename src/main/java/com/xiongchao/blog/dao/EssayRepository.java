package com.xiongchao.blog.dao;


import com.xiongchao.blog.bean.BasePage;
import com.xiongchao.blog.bean.Essay;
import org.springframework.data.domain.Page;
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

    /**
     * 查询用户当前文章的上一篇文章
      * @param id
     * @param userId
     * @return
     */
    @Query(value = "select * FROM essay where id < ?1 AND user_id = ?2 order by id DESC limit 1", nativeQuery = true)
    Essay findPreEssay(Integer id, Integer userId);

    /**
     * 查询用户当前文章的下一篇文章
     * @param id
     * @param userId
     * @return
     */
    @Query(value = "select * FROM essay where id > ?1 AND user_id = ?2 order by id asc limit 1", nativeQuery = true)
    Essay findNextEssay(Integer id, Integer userId);
}
