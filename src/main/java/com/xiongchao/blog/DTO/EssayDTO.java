package com.xiongchao.blog.DTO;

import com.xiongchao.blog.bean.Category;
import com.xiongchao.blog.bean.Essay;
import com.xiongchao.blog.bean.Tag;

import java.util.List;

public class EssayDTO extends Essay {

    private List<Tag> tags;

    private List<Category> categorys;

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
