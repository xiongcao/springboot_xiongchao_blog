package com.xiongchao.blog.dao;


import com.xiongchao.blog.bean.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TagRepository extends JpaRepository<Tag, Integer> {

    /**
     * 根据状态查询所有类型
     *
     * @param userId
     * @param status
     * @return
     */
    @Query(value = "select * from tag c WHERE c.user_id = ?1 and c.status = ?2 ORDER BY c.rank DESC", nativeQuery = true)
    List<Tag> findAllByUserIdAndStatus(Integer userId, Integer status);


    Tag findByIdAndUserId(Integer id, Integer userId);

    /**
     * 查询所有类型
     *
     * @param userId
     * @return
     */
    List<Tag> findAllByUserId(Integer userId);

    /**
     * 查询所有未删除且根据rank降序排序的类型
     *
     * @param userId
     * @return
     */
    @Query(value = "select * from tag c WHERE c.user_id = ? and c.status <> -1 ORDER BY c.rank DESC", nativeQuery = true)
    List<Tag> findAllByUserIdAndStatus(Integer userId);


    /**
     * 根据文章ID查询所有标签
     *
     * @param essayId
     * @return
     */
    @Query(value = "SELECT t.* FROM tag t LEFT JOIN essay_tag_mapping et ON t.id = et.tag_id WHERE et.essay_id = ? AND t.status <> 0", nativeQuery = true)
    List<Tag> findListByEssayId(Integer essayId);

    /**
     * 根据文章id和标签id查询 标签信息
     *
     * @param essayId
     * @param tagId
     * @return
     */
    @Query(value = "SELECT t.* FROM tag t LEFT JOIN essay_tag_mapping et ON t.id = et.tag_id WHERE et.essay_id = ?1 AND t.id = ?2", nativeQuery = true)
    Tag findByEssayIdAndTagId(Integer essayId, Integer tagId);

}
