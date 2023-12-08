package com.y.ccount.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Pavan on 6/5/2018.
 */
public class Post {

    @SerializedName("post")
    @Expose
    private PostData post;

    public PostData getPost() {
        return post;
    }

    public void setPost(PostData post) {
        this.post = post;
    }
}
