package com.y.ccount.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Pavan on 6/5/2018.
 */
public class PostData {
    @SerializedName("locid")
    @Expose
    private String locid;
    @SerializedName("locationdec")
    @Expose
    private String locationdec;
    @SerializedName("date")
    @Expose
    private String date;

    public String getLocid() {
        return locid;
    }

    public void setLocid(String locid) {
        this.locid = locid;
    }

    public String getLocationdec() {
        return locationdec;
    }

    public void setLocationdec(String locationdec) {
        this.locationdec = locationdec;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
