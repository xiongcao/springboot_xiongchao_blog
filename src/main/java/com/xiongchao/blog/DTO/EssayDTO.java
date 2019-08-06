package com.xiongchao.blog.DTO;

import com.xiongchao.blog.bean.Category;
import com.xiongchao.blog.bean.Comment;
import com.xiongchao.blog.bean.Essay;
import com.xiongchao.blog.bean.Tag;

import java.util.List;

public class EssayDTO extends Essay {

    private Integer commentNumber;

    private List<Tag> tags;

    private List<Category> categorys;

    private List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }

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
}
