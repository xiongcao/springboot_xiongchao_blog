package com.xiongchao.blog.DTO;

import com.xiongchao.blog.bean.Category;
import com.xiongchao.blog.bean.Essay;
import com.xiongchao.blog.bean.Tag;

import java.util.List;

public class EssayDTO extends Essay {

    private List<Tag> tags;

    private List<Category> categories;

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
}
