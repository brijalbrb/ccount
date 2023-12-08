package com.y.ccount.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Pavan on 12/12/2018.
 */
public class LoginPostData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("uname")
    @Expose
    private String uname;
    @SerializedName("pass")
    @Expose
    private String pass;
    @SerializedName("lastlogin")
    @Expose
    private String lastlogin;
    @SerializedName("activestatus")
    @Expose
    private String activestatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(String lastlogin) {
        this.lastlogin = lastlogin;
    }

    public String getActivestatus() {
        return activestatus;
    }

    public void setActivestatus(String activestatus) {
        this.activestatus = activestatus;
    }

}
