package com.y.ccount.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Pavan on 12/12/2018.
 */
public class LoginPost {
    @SerializedName("post")
    @Expose
    private LoginPostData post;

    public LoginPostData getPost() {
        return post;
    }

    public void setPost(LoginPostData post) {
        this.post = post;
    }
}
