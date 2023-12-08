package com.y.ccount.interfaces;

import com.y.ccount.apicallback.ItemCallBack;
import com.y.ccount.apicallback.LocationCallBack;
import com.y.ccount.apicallback.LoginCallBack;
import com.y.ccount.apicallback.SendCallBack;
import com.y.ccount.apicallback.UserCallBack;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Pavan on 6/5/2018.
 */
public interface ApiInterface {
    @GET("location_user")
    Call<LocationCallBack> location(@Query("uid") Integer userID, @Query("format") String format);

    @POST("userws?format=json")
    Call<UserCallBack> user();


    @GET("getdata?format=json")
    Call<ItemCallBack> item(@Query("nxt") Integer nxt, @Query("uid") Integer uid);

    @GET("insdata.php")
    Call<SendCallBack> send(@Query("itemid") String itemID, @Query("barcode") String barCode, @Query("recqty") String resQty, @Query("userid") String userId, @Query("location") String location, @Query("zone") String zone, @Query("bin") String bin, @Query("readmode") String readmode, @Query("readdatetime") String datettime, @Query("seqid") String seqid, @Query("snddt") String snddt, @Query("uom") String mou);

    @GET("userdata.php?format=json")
    Call<LoginCallBack> callLogin(@Query("uname") String email, @Query("pass") String password);

    /* @GET("insdata")
     Call<SendCallBack> sendData(@Query("itemid") String itemid, @Query("barcode") String barcode, @Query("recqty") String recqty, @Query("userid") String user_id,
                                 @Query("location") String location, @Query("zone") String zone, @Query("bin") String bin, @Query("readmode") String readMode,
                                 @Query("readdatetime") String readDateTime, @Query("seqid") String seqID, @Query("snddt") String currentDateandTime, @Query("uom") String mou, @Query("dec") String description);
 */
    @FormUrlEncoded
    @POST("insdata_bulk.php")
    Call<SendCallBack> sendData(@Field("jsonData") JSONObject finalJsonObject);
}
