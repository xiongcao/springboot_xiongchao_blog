package com.xiongchao.blog.DTO;

import com.xiongchao.blog.bean.*;

import java.util.List;

public class EssayDTO extends Essay {

    private Integer commentNumber;

    private List<Tag> tags;

    private List<Category> categorys;

    private List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }

    private Star star;

    private Collect collect;

    private User user;

    private Essay preEssay;

    private Essay nextEssay;

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Integer getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(Integer commentNumber) {
        this.commentNumber = commentNumber;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Category> getCategorys() {
        return categorys;
    }

    public void setCategorys(List<Category> categorys) {
        this.categorys = categorys;
    }

    public Star getStar() {
        return star;
    }

    public void setStar(Star star) {
        this.star = star;
    }

    public Collect getCollect() {
        return collect;
    }

    public void setCollect(Collect collect) {
        this.collect = collect;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Essay getPreEssay() {
        return preEssay;
    }

    public void setPreEssay(Essay preEssay) {
        this.preEssay = preEssay;
    }

    public Essay getNextEssay() {
        return nextEssay;
    }

    public void setNextEssay(Essay nextEssay) {
        this.nextEssay = nextEssay;
    }
}
