package com.xiongchao.blog.DTO;

import com.xiongchao.blog.bean.Collect;

public class CollectDTO extends Collect {

    private String title; // 文章标题

    private String name; // 作者账号名

    private String nickname; // 作者昵称

    private String remark; // 备注

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
}
