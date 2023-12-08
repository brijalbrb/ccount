package com.y.ccount.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Pavan on 6/5/2018.
 */
public class Item {
    @SerializedName("post")
    @Expose
    private ItemData post;

    public ItemData getPost() {
        return post;
    }

    public void setPost(ItemData post) {
        this.post = post;
    }
}
