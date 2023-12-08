package com.y.ccount.apicallback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.y.ccount.model.Item;

/**
 * Created by Pavan on 6/5/2018.
 */
public class ItemCallBack {
    @SerializedName("posts")
    @Expose
    private List<Item> posts = null;

    public List<Item> getPosts() {
        return posts;
    }

    public void setPosts(List<Item> posts) {
        this.posts = posts;
    }

}
