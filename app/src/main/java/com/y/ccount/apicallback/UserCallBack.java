package com.y.ccount.apicallback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.y.ccount.model.User;

/**
 * Created by Pavan on 6/5/2018.
 */
public class UserCallBack {
    @SerializedName("posts")
    @Expose
    private List<User> posts = null;

    public List<User> getPosts() {
        return posts;
    }

    public void setPosts(List<User> posts) {
        this.posts = posts;
    }
}
