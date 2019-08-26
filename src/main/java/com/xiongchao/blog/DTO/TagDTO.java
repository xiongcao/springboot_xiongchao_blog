package com.xiongchao.blog.DTO;

import com.xiongchao.blog.bean.Tag;

public class TagDTO extends Tag {
    private int essay_number;

    public int getEssay_number() {
        return essay_number;
    }

    public void setEssay_number(int essay_number) {
        this.essay_number = essay_number;
    }
}
