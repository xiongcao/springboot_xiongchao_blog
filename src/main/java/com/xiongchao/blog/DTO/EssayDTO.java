package com.xiongchao.blog.DTO;

import com.xiongchao.blog.bean.Category;
import com.xiongchao.blog.bean.Comment;
import com.xiongchao.blog.bean.Essay;
import com.xiongchao.blog.bean.Tag;

import java.util.List;

public class EssayDTO extends Essay {

    private List<Tag> tags;

    private List<Category> categories;

    private List<Comment> comments;

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
