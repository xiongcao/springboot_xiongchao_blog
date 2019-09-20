package com.xiongchao.blog.dao;


import com.xiongchao.blog.bean.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Optional<Project> findById(Integer id);

    List<Project> findAllByUserId(Integer userId);
}
