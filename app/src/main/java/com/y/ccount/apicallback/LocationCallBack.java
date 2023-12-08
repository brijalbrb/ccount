package com.y.ccount.apicallback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.y.ccount.model.Post;

/**
 * Created by Pavan on 6/5/2018.
 */
public class LocationCallBack {
    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
