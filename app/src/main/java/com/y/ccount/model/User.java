package com.y.ccount.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Pavan on 6/5/2018.
 */
public class User {
    @SerializedName("post")
    @Expose
    private UserData post;

    public UserData getPost() {
        return post;
    }

    public void setPost(UserData post) {
        this.post = post;
    }
}
