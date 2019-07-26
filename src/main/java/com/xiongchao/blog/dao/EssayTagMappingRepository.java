package com.xiongchao.blog.dao;

import com.xiongchao.blog.bean.EssayTagMapping;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EssayTagMappingRepository extends JpaRepository<EssayTagMapping, Integer> {

    /**
     * 根据文章id删除标签映射表数据
     * @param essayId
     * @return
     */
    Integer deleteByEssayId(Integer essayId);

}
