package com.sudarshan.clickit;


import java.sql.Timestamp;
import java.util.Date;

public class BlogPost extends BlogPostId{

    public String user_id, image_url, desc, title;
    public Date time_stamp;



//    public BlogPost(Date time_stamp) {
//        this.time_stamp = time_stamp;
//    }



    public BlogPost(){}

    public BlogPost(String user_id, String image_url, String desc, String title, Date time_stamp) {
        this.user_id = user_id;
        this.image_url = image_url;
        this.desc = desc;
        this.title = title;
        this.time_stamp = time_stamp;
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

    public Date getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Date time_stamp) {
        this.time_stamp = time_stamp;
    }



}
