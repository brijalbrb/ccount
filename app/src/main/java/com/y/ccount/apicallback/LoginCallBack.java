package com.y.ccount.apicallback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.y.ccount.model.LoginPost;

/**
 * Created by Pavan on 12/12/2018.
 */
public class LoginCallBack {
    @SerializedName("posts")
    @Expose
    private List<LoginPost> posts = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public List<LoginPost> getPosts() {
        return posts;
    }

    public void setPosts(List<LoginPost> posts) {
        this.posts = posts;
    }

}
