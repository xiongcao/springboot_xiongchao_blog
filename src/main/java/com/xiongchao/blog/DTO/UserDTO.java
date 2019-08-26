package com.xiongchao.blog.DTO;

import com.xiongchao.blog.bean.User;

public class UserDTO extends User {
    private int follow_number;

    private int fans_number;

    private int collect_number;

    private int tag_number;

    private int category_number;

    private int essay_number;

    public int getFollow_number() {
        return follow_number;
    }

    public void setFollow_number(int follow_number) {
        this.follow_number = follow_number;
    }

    public int getFans_number() {
        return fans_number;
    }

    public void setFans_number(int fans_number) {
        this.fans_number = fans_number;
    }

    public int getCollect_number() {
        return collect_number;
    }

    public void setCollect_number(int collect_number) {
        this.collect_number = collect_number;
    }

    public int getTag_number() {
        return tag_number;
    }

    public void setTag_number(int tag_number) {
        this.tag_number = tag_number;
    }

    public int getCategory_number() {
        return category_number;
    }

    public void setCategory_number(int category_number) {
        this.category_number = category_number;
    }

    public int getEssay_number() {
        return essay_number;
    }

    public void setEssay_number(int essay_number) {
        this.essay_number = essay_number;
    }
}
