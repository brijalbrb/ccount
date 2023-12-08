package com.y.ccount.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Pavan on 6/5/2018.
 */
public class ItemData {
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("itemid")
    @Expose
    private String itemid;
    @SerializedName("zone")
    @Expose
    private String zone;
    @SerializedName("mou")
    @Expose
    private String mou;
    @SerializedName("readmode")
    @Expose
    private String readMode;
    @SerializedName("readdatetime")
    @Expose
    private String readDateTime;
    @SerializedName("seqid")
    @Expose
    private String seqID;
    @SerializedName("des")
    @Expose
    private String des;
    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("bin")
    @Expose
    private String bin;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("recqty")
    @Expose
    private String recqty;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("datetime")
    @Expose
    private String datetime;
    @SerializedName("jobid")
    @Expose
    private String jobid;
    @SerializedName("nxt")
    @Expose
    private String nxt;
    @SerializedName("location_id")
    @Expose
    private String locationID;
    @SerializedName("selectedLocation")
    @Expose
    public String selectedLocation;

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getMou() {
        return mou;
    }

    public void setMou(String mou) {
        this.mou = mou;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRecqty() {
        return recqty;
    }

    public void setRecqty(String recqty) {
        this.recqty = recqty;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getJobid() {
        return jobid;
    }

    public String getReadMode() {
        return readMode;
    }

    public void setReadMode(String readMode) {
        this.readMode = readMode;
    }

    public String getReadDateTime() {
        return readDateTime;
    }

    public void setReadDateTime(String readDateTime) {
        this.readDateTime = readDateTime;
    }

    public String getSeqID() {
        return seqID;
    }

    public void setSeqID(String seqID) {
        this.seqID = seqID;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getNxt() {
        return nxt;
    }

    public void setNxt(String nxt) {
        this.nxt = nxt;
    }

    public String getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(String selectedLocation) {
        this.selectedLocation = selectedLocation;
    }
}
