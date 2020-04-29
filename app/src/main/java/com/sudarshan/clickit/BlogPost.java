package com.sudarshan.clickit;

import java.sql.Timestamp;

public class BlogPost {

    public String user_id, image_url, desc, title;

    public BlogPost(){}

    public BlogPost(String user_id, String image_url, String desc, String title, Timestamp time_stamp) {
        this.user_id = user_id;
        this.image_url = image_url;
        this.desc = desc;
        this.title = title;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
