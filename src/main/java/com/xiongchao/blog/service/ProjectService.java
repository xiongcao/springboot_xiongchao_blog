package com.xiongchao.blog.service;

import com.xiongchao.blog.bean.Project;
import com.xiongchao.blog.dao.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Cacheable(value = "findProjectById", key = "#id")
    public Optional<Project> findById(Integer id){
        return projectRepository.findById(id);
    }

    public List<Project> findAllByUserId(Integer userId) {
        return projectRepository.findAllByUserId(userId);
    }
}
