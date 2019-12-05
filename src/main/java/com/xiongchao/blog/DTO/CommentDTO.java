package com.xiongchao.blog.DTO;

import com.xiongchao.blog.bean.Comment;
import com.xiongchao.blog.bean.User;

public class CommentDTO extends Comment {

    private String title; // 文章标题

    private String name; // 文章作者

    private String nickname; // 作者昵称

    private String remark; // 作者备注

    private User user; // 评论者

    private User toUser; // 被评论者

    private Integer commentLikeStatus; // 点赞状态：1: 已赞 0: 踩 -1: 未点赞、未踩

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public Integer getCommentLikeStatus() {
        return commentLikeStatus;
    }

    public void setCommentLikeStatus(Integer commentLikeStatus) {
        this.commentLikeStatus = commentLikeStatus;
    }
}
