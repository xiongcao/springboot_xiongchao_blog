package com.xiongchao.blog.service;

import com.xiongchao.blog.bean.PageWithSearch;
import com.xiongchao.blog.bean.User;
import com.xiongchao.blog.dao.UserRepository;
import com.xiongchao.blog.util.SqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowService followService;

    @Autowired
    private EntityManager em;

    public User findByUsername (String name) {
        return userRepository.findByName(name);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public  User findByPhoneNumber(String phone) {
        return userRepository.findByPhoneNumber(phone);
    }

    @Cacheable(value = "findUserById", key = "#id")
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    /**
     * 查询用户的关注、粉丝数量
     * @param userId 用户ID
     * @param status 1：关注 2：粉丝
     * @return
     */
    public Integer findFollowNumberByStatus(Integer userId, Integer status) {
        return userRepository.findFollowNumberByStatus(userId, status);
    }


    public Page<User> findUserAll(PageWithSearch pageWithSearch){
        String field = pageWithSearch.getField();
        String value = pageWithSearch.getValue();
        Integer page = pageWithSearch.defaultPage(0);
        Integer size = pageWithSearch.defaultSize(20);

        StringBuilder sb = new StringBuilder();
        sb.append(" select " + SqlUtil.sqlGenerate("u", User.class) + " from user u where 1 = 1 ");
//        if (null != userId) sb.append(" and u.id = " + userId + " "); // 超管查询所有用户信息

        if (!StringUtils.isEmpty(field) && !StringUtils.isEmpty(value)) {   // 搜索条件
            sb.append(" and u." + SqlUtil.camelToUnderline(field) + " like '"+ value +"' ");
        }
        sb.append(" ORDER BY u.created_date DESC");

        Query query = em.createNativeQuery(sb.toString());
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        List<Object> result = query.getResultList();
        List<User> users = new ArrayList<>();
        for (Object o : result) {
            Object[] obj = (Object[]) o;
            users.add(SqlUtil.toBean(obj, User.class));
        }
        if (result.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);
        }

        return new PageImpl<>(users, PageRequest.of(page, size), findListCount(pageWithSearch));

    }

    public Long findListCount(PageWithSearch pageWithSearch) {
        String field = pageWithSearch.getField();
        String value = pageWithSearch.getValue();

        StringBuilder sb = new StringBuilder();
        sb.append(" select count(*) from user u where 1 = 1 ");
        if (!StringUtils.isEmpty(field) && !StringUtils.isEmpty(value)) {//无搜索条件
            sb.append(" and u." + SqlUtil.camelToUnderline(field) + " like '"+ value +"' ");
        }

        Query query = em.createNativeQuery(sb.toString());
        List<Object> objects = query.getResultList();
        return Long.parseLong(objects.get(0).toString());
    }
}
