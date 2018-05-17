package com.template.bean;

import java.io.Serializable;

/**
 * 实体类
 */
public class Test implements Serializable {

    private String title;
    private String content;
    private String img;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
