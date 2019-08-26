package com.xiongchao.blog.service;

import com.xiongchao.blog.bean.Follow;
import com.xiongchao.blog.bean.PageWithSearch;
import com.xiongchao.blog.dao.FollowRepository;
import com.xiongchao.blog.util.SqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FollowService {

    @Autowired
    private EntityManager em;

    @Autowired
    private FollowRepository followRepository;

    public Follow saveFollow(Follow follow){
        return followRepository.save(follow);
    }

    public List<Follow> findFollowAll(Integer userId){
        return followRepository.findByUserId(userId);
    }

    public Optional<Follow> findById(Integer id){
        return followRepository.findById(id);
    }

    public Follow findByUserIdAndFollowUserId(Integer userId, Integer followUserId, Integer status){
        return followRepository.findByUserIdAndFollowUserId(userId, followUserId, status);
    }

    public Page<Follow> findFollowAll(PageWithSearch pageWithSearch, Integer userId, Integer status){
        String field = pageWithSearch.getField();
        String value = pageWithSearch.getValue();
        Integer page = pageWithSearch.defaultPage(0);
        Integer size = pageWithSearch.defaultSize(1000);

        StringBuilder sb = new StringBuilder();
        sb.append(" select " + SqlUtil.sqlGenerate("u", Follow.class) + " from follow u where u.status = " + status + " ");
        if (null != userId) sb.append(" and u.user_id = " + userId + " "); // 超管查询所有用户信息

        if (!StringUtils.isEmpty(field) && !StringUtils.isEmpty(value)) {   // 搜索条件
            sb.append(" and u." + SqlUtil.camelToUnderline(field) + " like '%"+ value +"%' ");
        }
        sb.append(" ORDER BY u.created_date DESC");

        Query query = em.createNativeQuery(sb.toString());
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        List<Object> result = query.getResultList();
        List<Follow> follows = new ArrayList<>();
        for (Object o : result) {
            Object[] obj = (Object[]) o;
            Follow follow = SqlUtil.toBean(obj, Follow.class);
            // 查询被关注或粉丝用户是否有我
            Boolean mutualWatch = false;
            if (follow.getStatus() == 1) {  // 关注, 他的关注里是否有我
                Follow follow1 = findByUserIdAndFollowUserId(follow.getFollowUserId(), follow.getUserId(), follow.getStatus());
                if (follow1 != null) {
                    mutualWatch = true;
                }
            } else {    // 粉丝， 我的关注里是否有他
                Follow follow1 = findByUserIdAndFollowUserId(follow.getUserId(), follow.getFollowUserId(), 1);
                if (follow1 != null) {
                    mutualWatch = true;
                }
            }
            follow.setMutualWatch(mutualWatch);
            follows.add(follow);
        }
        if (result.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);
        }

        return new PageImpl<>(follows, PageRequest.of(page, size), findListCount(pageWithSearch, userId, status));
    }

    public Long findListCount(PageWithSearch pageWithSearch, Integer userId, Integer status) {
        String field = pageWithSearch.getField();
        String value = pageWithSearch.getValue();

        StringBuilder sb = new StringBuilder();
        sb.append(" select count(*) from follow u where u.user_id = " + userId + " and u.status = " + status + " ");
        if (!StringUtils.isEmpty(field) && !StringUtils.isEmpty(value)) {//无搜索条件
            sb.append(" and u." + SqlUtil.camelToUnderline(field) + " like '%"+ value +"%' ");
        }
        Query query = em.createNativeQuery(sb.toString());
        List<Object> objects = query.getResultList();
        return Long.parseLong(objects.get(0).toString());
    }

}
