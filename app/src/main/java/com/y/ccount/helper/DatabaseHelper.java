package com.y.ccount.helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.y.ccount.apicallback.ItemCallBack;
import com.y.ccount.apicallback.LocationCallBack;
import com.y.ccount.apicallback.UserCallBack;
import com.y.ccount.model.DatabaseModel;
import com.y.ccount.model.ItemData;
import com.y.ccount.model.ItemDummyModel;
import com.y.ccount.model.LocationModel;

import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Pavan on 6/5/2018.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Ccount.db";
    public static final String LOCATION_TABLE = "location_table";
    public static final String USER_TABLE = "user_table";
    public static final String ITEM_TABLE = "item_table";
    public static final String LOCATION_ID = "ID";
    public static final String LOCATION_DESC = "DESCRIPTION";
    public static final String DATE = "DATE";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String LASTLOGIN = "LASTLOGIN";
    public static final String ACTIVESTATUS = "ACTIVESTATUS";
    public static final String SELECTEDLOCATION = "SELECTEDLOCATION";
    //for item_table
    public static final String USER_ID = "USERID";
    public static final String ITEM_ID = "ITEMID";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String ZONE = "ZONE";
    public static final String BARCODE = "BARCODE";
    public static final String BIN = "BIN";
    public static final String QTY = "QTY";
    public static final String RECQTY = "RECQTY";
    public static final String LOCATION = "LOCATION";
    public static final String DATETIME = "DATETIME";
    public static final String JOBID = "JOBID";
    public static final String NXT = "NXT";
    public static final String STATUS = "STATUS";
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String MOU = "MOU";
    public static final String READMODE = "READMODE";
    public static final String READDATETIME = "READDATETIME";
    public static final String SEQID = "SEQID";
    public static final String UPLOADSTATUS = "UPLOADSTATUS";

    //    public static final String COL_4 = "MARKS";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + LOCATION_TABLE + " (ID TEXT,DESCRIPTION TEXT,DATE TEXT)");
        db.execSQL("create table " + USER_TABLE + " (ID TEXT,USERNAME TEXT,PASSWORD TEXT,LASTLOGIN TEXT,ACTIVESTATUS TEXT)");
        db.execSQL("create table " + ITEM_TABLE + " (USERID TEXT,ITEMID TEXT,ZONE TEXT,MOU TEXT,DESCRIPTION TEXT,BARCODE TEXT,BIN TEXT,QTY TEXT,RECQTY TEXT,LOCATION TEXT,DATETIME TEXT,JOBID TEXT,NXT TEXT,READMODE TEXT,READDATETIME TEXT,SEQID TEXT,UPLOADSTATUS TEXT,ID TEXT,SELECTEDLOCATION TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
        onCreate(db);
    }


    public boolean inserData(int position, Response<LocationCallBack> response) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOCATION_ID, response.body().getPosts().get(position).getPost().getLocid());
        contentValues.put(LOCATION_DESC, response.body().getPosts().get(position).getPost().getLocationdec());
//        contentValues.put(DATE, response.body().getPosts().get(position).getPost().getDate());
        long result = db.insert(LOCATION_TABLE, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

//    public Cursor getAllData() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor res = db.rawQuery("select * from " + LOCATION_TABLE, null);
//        return res;
//    }

    public boolean insertUserData(int position, Response<UserCallBack> response) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOCATION_ID, response.body().getPosts().get(position).getPost().getId());
        contentValues.put(USERNAME, response.body().getPosts().get(position).getPost().getUname());
        contentValues.put(PASSWORD, response.body().getPosts().get(position).getPost().getPass());
        contentValues.put(LASTLOGIN, response.body().getPosts().get(position).getPost().getLastlogin());
        contentValues.put(ACTIVESTATUS, response.body().getPosts().get(position).getPost().getActivestatus());
        long result = 0;
        try {
            result = db.insert(USER_TABLE, null, contentValues);

        } catch (Exception e) {
            Log.d("checkException", "insertUserData: ");
        }
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkUser(Context context, String email, String password) {
        // array of columns to fetch
        String[] columns = {
                LOCATION_ID, USERNAME, PASSWORD, LASTLOGIN, ACTIVESTATUS
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = USERNAME + " = ?" + " AND " + PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(USER_TABLE, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();


        if (cursorCount > 0) {
            if (cursor.moveToFirst()) {

                SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("userId", cursor.getString(cursor.getColumnIndex(LOCATION_ID)));
//                Toast.makeText(context, "id is" + cursor.getString(cursor.getColumnIndex(LOCATION_ID)), Toast.LENGTH_LONG).show();
                editor.apply();
            }
            return true;
        }
        cursor.close();
        db.close();

        return false;
    }

   /* public boolean inserItemData(ArrayList<ItemDummyModel> itemDummyModels) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        for (int i = 0; i < itemDummyModels.size(); i++) {
            Log.d("checkBarCode", "inserItemData: " + itemDummyModels.get(i).barCode);
            ContentValues contentValues = new ContentValues();
            contentValues.put(USER_ID, itemDummyModels.get(i).userId);
            contentValues.put(ITEM_ID, itemDummyModels.get(i).itemID);
            contentValues.put(ZONE, itemDummyModels.get(i).zone);
            contentValues.put(BARCODE, itemDummyModels.get(i).barCode);
            contentValues.put(BIN, itemDummyModels.get(i).bin);
            contentValues.put(QTY, itemDummyModels.get(i).qty);
            contentValues.put(RECQTY, itemDummyModels.get(i).resQty);
            contentValues.put(LOCATION, itemDummyModels.get(i).location);
            contentValues.put(DATETIME, itemDummyModels.get(i).dateTime);
            contentValues.put(JOBID, itemDummyModels.get(i).jobId);
            contentValues.put(NXT, itemDummyModels.get(i).nxt);
            contentValues.put(STATUS, itemDummyModels.get(i).status);
            contentValues.put(MOU, itemDummyModels.get(i).mou);
            try {
                result = db.insert(ITEM_TABLE, null, contentValues);

            } catch (Exception e) {
                Log.d("checkException", "insertUserData: ");
            }

        }
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }*/

    public ArrayList<ItemDummyModel> getItemData(String barCode) {
        ArrayList<ItemDummyModel> itemDummyModels = new ArrayList<>();
        String[] columns = {
                USER_ID, ITEM_ID, ZONE, BARCODE, BIN, QTY, RECQTY, LOCATION, DATETIME, JOBID, NXT, STATUS, MOU
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = BARCODE + " = ?";

        // selection arguments
        String[] selectionArgs = {barCode};
        Cursor cursor = db.query(ITEM_TABLE, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();
        if (cursorCount > 0) {
            if (cursor.moveToFirst()) {
                ItemDummyModel itemDummyModel = new ItemDummyModel();
                itemDummyModel.userId = cursor.getString(cursor.getColumnIndex(USER_ID));
                itemDummyModel.itemID = cursor.getString(cursor.getColumnIndex(ITEM_ID));
                itemDummyModel.zone = cursor.getString(cursor.getColumnIndex(ZONE));
                itemDummyModel.barCode = cursor.getString(cursor.getColumnIndex(BARCODE));
                itemDummyModel.bin = cursor.getString(cursor.getColumnIndex(BIN));
                itemDummyModel.qty = cursor.getString(cursor.getColumnIndex(QTY));
                itemDummyModel.resQty = cursor.getString(cursor.getColumnIndex(RECQTY));
                itemDummyModel.location = cursor.getString(cursor.getColumnIndex(LOCATION));
                itemDummyModel.dateTime = cursor.getString(cursor.getColumnIndex(DATETIME));
                itemDummyModel.jobId = cursor.getString(cursor.getColumnIndex(JOBID));
                itemDummyModel.nxt = cursor.getString(cursor.getColumnIndex(NXT));
                itemDummyModel.status = cursor.getString(cursor.getColumnIndex(STATUS));
                itemDummyModel.mou = cursor.getString(cursor.getColumnIndex(MOU));
                itemDummyModels.add(itemDummyModel);
            }
        }

        return itemDummyModels;
    }

    public boolean updateItemData(ArrayList<ItemData> itemDummyModelArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
//            Log.d("checkBarCode", "inserItemData: " + itemDummyModels.get(i).barCode);
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_ID, itemDummyModelArrayList.get(0).getItemid());
        contentValues.put(ZONE, itemDummyModelArrayList.get(0).getZone());
        contentValues.put(MOU, itemDummyModelArrayList.get(0).getMou());
        contentValues.put(DESCRIPTION, itemDummyModelArrayList.get(0).getDes());
        contentValues.put(BARCODE, itemDummyModelArrayList.get(0).getBarcode());
        contentValues.put(BIN, itemDummyModelArrayList.get(0).getBin());
        contentValues.put(QTY, itemDummyModelArrayList.get(0).getQty());
        contentValues.put(RECQTY, itemDummyModelArrayList.get(0).getRecqty());
        Log.d("checkQuantity", "updateItemData: " + itemDummyModelArrayList.get(0).getQty());
        contentValues.put(LOCATION, itemDummyModelArrayList.get(0).getLocation());
        contentValues.put(DATETIME, itemDummyModelArrayList.get(0).getDatetime());
        contentValues.put(JOBID, itemDummyModelArrayList.get(0).getJobid());
        contentValues.put(NXT, itemDummyModelArrayList.get(0).getNxt());
        contentValues.put(READMODE, itemDummyModelArrayList.get(0).getReadMode());
        contentValues.put(READDATETIME, itemDummyModelArrayList.get(0).getReadDateTime());
        contentValues.put(SEQID, itemDummyModelArrayList.get(0).getSeqID());
        contentValues.put(LOCATION_ID, itemDummyModelArrayList.get(0).getLocationID());
        contentValues.put(SELECTEDLOCATION, itemDummyModelArrayList.get(0).getSelectedLocation());
        contentValues.put(UPLOADSTATUS, "pending");
//        contentValues.put(UPLOADSTATUS, "pending");

        try {
//            result = db.insert(ITEM_TABLE, null, contentValues);
            result = db.update(ITEM_TABLE, contentValues, "" + ITEM_ID + " = ?", new String[]{itemDummyModelArrayList.get(0).getItemid()});
        } catch (Exception e) {
            Log.d("checkException", "insertUserData: " + e.getLocalizedMessage());
        }

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public ArrayList<ItemDummyModel> getAllItemData() {
        ArrayList<ItemDummyModel> itemDummyModels = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + ITEM_TABLE, null);
        int cursorCount = res.getCount();
        if (cursorCount > 0) {
            if (res.moveToFirst()) {
                do {
                    ItemDummyModel itemDummyModel = new ItemDummyModel();
                    itemDummyModel.userId = res.getString(res.getColumnIndex(USER_ID));
                    itemDummyModel.itemID = res.getString(res.getColumnIndex(ITEM_ID));
                    itemDummyModel.zone = res.getString(res.getColumnIndex(ZONE));
                    itemDummyModel.barCode = res.getString(res.getColumnIndex(BARCODE));
                    itemDummyModel.bin = res.getString(res.getColumnIndex(BIN));
                    itemDummyModel.qty = res.getString(res.getColumnIndex(QTY));
                    itemDummyModel.resQty = res.getString(res.getColumnIndex(RECQTY));
                    itemDummyModel.location = res.getString(res.getColumnIndex(LOCATION));
                    itemDummyModel.dateTime = res.getString(res.getColumnIndex(DATETIME));
                    itemDummyModel.jobId = res.getString(res.getColumnIndex(JOBID));
                    itemDummyModel.nxt = res.getString(res.getColumnIndex(NXT));
                    itemDummyModel.status = res.getString(res.getColumnIndex(STATUS));
                    itemDummyModel.mou = res.getString(res.getColumnIndex(MOU));

                    itemDummyModels.add(itemDummyModel);
                } while (res.moveToNext());
            }
            res.close();
            db.close();
        }
        return itemDummyModels;
    }

    public boolean insertNewItemData(String userID, String status, String uom, Response<ItemCallBack> response) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID, userID);
        contentValues.put(ITEM_ID, response.body().getPosts().get(0).getPost().getItemid());
        contentValues.put(ZONE, response.body().getPosts().get(0).getPost().getZone());
        contentValues.put(BARCODE, response.body().getPosts().get(0).getPost().getBarcode());
        contentValues.put(BIN, response.body().getPosts().get(0).getPost().getBin());
        contentValues.put(QTY, response.body().getPosts().get(0).getPost().getQty());
        contentValues.put(RECQTY, response.body().getPosts().get(0).getPost().getRecqty());
//        Log.d("checkQuantity", "updateItemData: " + itemDummyModelArrayList.get(0).resQty);
        contentValues.put(LOCATION, response.body().getPosts().get(0).getPost().getLocation());
        contentValues.put(DATETIME, response.body().getPosts().get(0).getPost().getDatetime());
        contentValues.put(JOBID, response.body().getPosts().get(0).getPost().getJobid());
        contentValues.put(NXT, response.body().getPosts().get(0).getPost().getNxt());
        contentValues.put(STATUS, status);
        contentValues.put(MOU, uom);
        long result = 0;
        try {
            result = db.insert(ITEM_TABLE, null, contentValues);

        } catch (Exception e) {
            Log.d("checkException", "insertUserData: ");
        }
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean deleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean value = false;
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        List<String> tables = new ArrayList<>();

// iterate over the result set, adding every table name to a list
        while (c.moveToNext()) {
            tables.add(c.getString(0));
        }

// call DROP TABLE on every table name
        for (String table : tables) {
            String dropQuery = "DELETE FROM " + table;
            try {
//                db.execSQL(dropQuery);
                int record = db.delete(table, "1", null);
                Log.d("checkRecord", "deleteData: deleted " + record + " from " + table);
                value = true;
            } catch (Exception e) {
                value = false;
            }
        }
        return value;
    }

    public boolean inserItemData(int i, Response<ItemCallBack> response) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_ID, response.body().getPosts().get(i).getPost().getItemid());
        contentValues.put(ZONE, response.body().getPosts().get(i).getPost().getZone());
        contentValues.put(MOU, response.body().getPosts().get(i).getPost().getMou());
        contentValues.put(DESCRIPTION, response.body().getPosts().get(i).getPost().getDes());
        contentValues.put(BARCODE, response.body().getPosts().get(i).getPost().getBarcode());
        contentValues.put(BIN, response.body().getPosts().get(i).getPost().getBin());
        contentValues.put(QTY, response.body().getPosts().get(i).getPost().getQty());
        contentValues.put(RECQTY, response.body().getPosts().get(i).getPost().getRecqty());
        contentValues.put(LOCATION, response.body().getPosts().get(i).getPost().getLocation());
        contentValues.put(DATETIME, response.body().getPosts().get(i).getPost().getDatetime());
        contentValues.put(JOBID, response.body().getPosts().get(i).getPost().getJobid());
        contentValues.put(NXT, response.body().getPosts().get(i).getPost().getNxt());
        contentValues.put(READMODE, "");
        contentValues.put(READDATETIME, "");
        contentValues.put(SEQID, "");
        contentValues.put(UPLOADSTATUS, "pending");
        contentValues.put(LOCATION_ID, "");
        contentValues.put(SELECTEDLOCATION, "1");
        contentValues.put(USER_ID, "");
        long result = 0;
        try {
            result = db.insert(ITEM_TABLE, null, contentValues);

        } catch (Exception e) {
            Log.d("checkException", "insertUserData: ");
        }
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<ItemData> checkData(String barCode) {
        ArrayList<ItemData> itemDataArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + ITEM_TABLE + " WHERE " + BARCODE + "='" + barCode + "'", null);
        if (c != null) {
            try {


                if (c.moveToFirst()) {
                    do {
                        ItemData itemData = new ItemData();
                        itemData.setBarcode(c.getString(c.getColumnIndex(BARCODE)));
                        itemData.setBin(c.getString(c.getColumnIndex(BIN)));
                        itemData.setMou(c.getString(c.getColumnIndex(MOU)));
                        itemData.setDes(c.getString(c.getColumnIndex(DESCRIPTION)));
                        itemData.setDatetime(c.getString(c.getColumnIndex(DATETIME)));
                        itemData.setItemid(c.getString(c.getColumnIndex(ITEM_ID)));
                        itemData.setJobid(c.getString(c.getColumnIndex(JOBID)));
                        itemData.setLocation(c.getString(c.getColumnIndex(LOCATION)));
                        itemData.setNxt(c.getString(c.getColumnIndex(NXT)));
                        itemData.setQty(c.getString(c.getColumnIndex(QTY)));
                        itemData.setRecqty(c.getString(c.getColumnIndex(RECQTY)));
                        itemData.setZone(c.getString(c.getColumnIndex(ZONE)));
                        itemData.setReadMode(c.getString(c.getColumnIndex(READMODE)));
                        itemData.setReadDateTime(c.getString(c.getColumnIndex(READDATETIME)));
                        itemData.setSeqID(c.getString(c.getColumnIndex(SEQID)));
                        itemData.setLocationID(c.getString(c.getColumnIndex(LOCATION_ID)));
                        itemData.setSelectedLocation(c.getString(c.getColumnIndex(SELECTEDLOCATION)));
                        itemDataArrayList.add(itemData);
                    } while (c.moveToNext());
                }
            } catch (Exception e) {
                Log.d("checkException", "checkData: " + e.getLocalizedMessage());
            }
        }
        return itemDataArrayList;
    }

    public boolean inserItemDataCurrent(ArrayList<ItemData> itemDataArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID, itemDataArrayList.get(0).getUser_id());
        contentValues.put(ITEM_ID, itemDataArrayList.get(0).getItemid());
        contentValues.put(ZONE, itemDataArrayList.get(0).getZone());
        contentValues.put(MOU, itemDataArrayList.get(0).getMou());
        contentValues.put(DESCRIPTION, itemDataArrayList.get(0).getDes());
        contentValues.put(BARCODE, itemDataArrayList.get(0).getBarcode());
        contentValues.put(BIN, itemDataArrayList.get(0).getBin());
        contentValues.put(QTY, itemDataArrayList.get(0).getQty());
        contentValues.put(RECQTY, itemDataArrayList.get(0).getRecqty());
        contentValues.put(LOCATION, itemDataArrayList.get(0).getLocation());
        contentValues.put(DATETIME, itemDataArrayList.get(0).getDatetime());
        contentValues.put(JOBID, itemDataArrayList.get(0).getJobid());
        contentValues.put(NXT, itemDataArrayList.get(0).getNxt());
        contentValues.put(READMODE, itemDataArrayList.get(0).getReadMode());
        contentValues.put(READDATETIME, itemDataArrayList.get(0).getReadDateTime());
        contentValues.put(SEQID, itemDataArrayList.get(0).getSeqID());
        contentValues.put(LOCATION_ID, itemDataArrayList.get(0).getLocationID());
        contentValues.put(UPLOADSTATUS, "pending");
        contentValues.put(SELECTEDLOCATION, itemDataArrayList.get(0).getSelectedLocation());
        long result = 0;
        try {
            result = db.insert(ITEM_TABLE, null, contentValues);

        } catch (Exception e) {
            Log.d("checkException", "insertUserData: " + e.getLocalizedMessage());
        }
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<LocationModel> getLocationData() {
        List<LocationModel> postDataArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + LOCATION_TABLE + "", null);
        if (c != null) {
            try {


                if (c.moveToFirst()) {
                    do {
                        LocationModel locationModel = new LocationModel();
                        locationModel.setName(c.getString(c.getColumnIndex(LOCATION_DESC)));
                        locationModel.setId(c.getString(c.getColumnIndex(LOCATION_ID)));
                        postDataArrayList.add(locationModel);
                    } while (c.moveToNext());
                }
            } catch (Exception e) {
                Log.d("checkException", "checkData: " + e.getLocalizedMessage());
            }
        }
        return postDataArrayList;
    }

    public String getLastItemId() {
        String item_id = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + ITEM_TABLE + "", null);
        if (c != null) {
            try {
                if (c.moveToLast()) {
                    item_id = c.getString(c.getColumnIndex(ITEM_ID));
                }
            } catch (Exception e) {
                Log.d("checkException", "checkData: " + e.getLocalizedMessage());
            }
        }
        return item_id;
    }

    public String getLastSeqId() {
        String seq_id = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + ITEM_TABLE + "", null);
        if (c != null) {
            try {
                if (c.moveToLast()) {
                    seq_id = c.getString(c.getColumnIndex(SEQID));
                }
            } catch (Exception e) {
                Log.d("checkException", "checkData: " + e.getLocalizedMessage());
            }
        }
        return seq_id;
    }

    public int getCount(int value) {
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        if (value == 0) {
            c = db.rawQuery("SELECT * FROM " + ITEM_TABLE + " WHERE " + READMODE + " = 'SingleMode' AND " + UPLOADSTATUS + " = 'pending'", null);
        } else if (value == 1) {
            c = db.rawQuery("SELECT * FROM " + ITEM_TABLE + " WHERE " + READMODE + " = 'MultiMode' AND " + UPLOADSTATUS + " = 'pending'", null);
        } else {
            c = db.rawQuery("SELECT * FROM " + ITEM_TABLE + " WHERE " + READMODE + " = 'BlindMode' AND " + UPLOADSTATUS + " = 'pending'", null);
        }

        if (c != null) {
            try {
                result = c.getCount();
            } catch (Exception e) {
                Log.d("checkException", "checkData: " + e.getLocalizedMessage());
            }
        }
        return result;
    }

    public ArrayList<ItemData> getAllItemsData(int starting, int ending) {
        ArrayList<ItemData> itemDataArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + ITEM_TABLE + " WHERE  ITEMID IN (SELECT ITEMID FROM item_table limit " + starting + "," + ending + ") AND " + UPLOADSTATUS + " = 'pending'";

        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    do {
                        ItemData itemData = new ItemData();
                        itemData.setUser_id(c.getString(c.getColumnIndex(USER_ID)));
                        itemData.setBarcode(c.getString(c.getColumnIndex(BARCODE)));
                        itemData.setBin(c.getString(c.getColumnIndex(BIN)));
                        itemData.setMou(c.getString(c.getColumnIndex(MOU)));
                        itemData.setDes(c.getString(c.getColumnIndex(DESCRIPTION)));
                        itemData.setDatetime(c.getString(c.getColumnIndex(DATETIME)));
                        itemData.setItemid(c.getString(c.getColumnIndex(ITEM_ID)));
                        itemData.setJobid(c.getString(c.getColumnIndex(JOBID)));
                        itemData.setLocation(c.getString(c.getColumnIndex(LOCATION)));
                        itemData.setNxt(c.getString(c.getColumnIndex(NXT)));
                        itemData.setQty(c.getString(c.getColumnIndex(QTY)));
                        itemData.setRecqty(c.getString(c.getColumnIndex(RECQTY)));
                        itemData.setZone(c.getString(c.getColumnIndex(ZONE)));
                        itemData.setReadMode(c.getString(c.getColumnIndex(READMODE)));
                        itemData.setReadDateTime(c.getString(c.getColumnIndex(READDATETIME)));
                        itemData.setSeqID(c.getString(c.getColumnIndex(SEQID)));
                        itemData.setLocationID(c.getString(c.getColumnIndex(LOCATION_ID)));
                        itemData.setSelectedLocation(c.getString(c.getColumnIndex(SELECTEDLOCATION)));
                        itemDataArrayList.add(itemData);
                    } while (c.moveToNext());
                }
            } catch (Exception e) {
                Log.d("checkException", "checkData: " + e.getLocalizedMessage());
            }
        }
        return itemDataArrayList;
    }

    public boolean updatePendingData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UPLOADSTATUS, "completed");
        int check = db.update(ITEM_TABLE, contentValues, "" + UPLOADSTATUS + " = 'pending'", null);
        if (check > 0) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean deleteUserData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        int check = db.delete(USER_TABLE, null, null);
        if (check > 0) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean checkUserData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + USER_TABLE + "", null);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    result = true;
                }
            } catch (Exception e) {
                Log.d("checkException", "checkData: " + e.getLocalizedMessage());
            }
        }
        return result;
    }

    public boolean checkItemData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + LOCATION_TABLE + "", null);
        if (c != null) {
            try {
                Log.d("checkCursor", "checkItemData: " + c.getCount());
                if (c.moveToFirst()) {
                    result = true;
                }
            } catch (Exception e) {
                Log.d("checkException", "checkData: " + e.getLocalizedMessage());
            }
        }
        return result;
    }

    public int getTotalNumberOfRecords() {
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        c = db.rawQuery("SELECT * FROM " + ITEM_TABLE + " WHERE " + UPLOADSTATUS + " = 'pending'", null);
        if (c != null) {
            try {
                result = c.getCount();
            } catch (Exception e) {
                Log.d("checkException", "checkData: " + e.getLocalizedMessage());
            }
        }
        return result;
    }

    public int getALLNumberOfRecords() {
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        c = db.rawQuery("SELECT * FROM " + ITEM_TABLE + "", null);
        if (c != null) {
            try {
                result = c.getCount();
            } catch (Exception e) {
                Log.d("checkException", "checkData: " + e.getLocalizedMessage());
            }
        }
        return result;
    }

    public ArrayList<DatabaseModel> getAllItemsDataNew(int starting, int ending) {
        ArrayList<DatabaseModel> databaseModelArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + ITEM_TABLE + " WHERE  ITEMID IN (SELECT ITEMID FROM item_table limit " + starting + "," + ending + ") AND " + UPLOADSTATUS + " = 'pending'";

        Cursor c = db.rawQuery(query, null);
        try {
            if (c.moveToFirst()) {
                do {
                    int columnCount = c.getColumnCount();
                    for (int i = 0; i < columnCount; i++) {
                        String columnName = c.getColumnName(i);
                        String columnValue = c.getString(c.getColumnIndex(columnName));
                        if (!columnName.equals(SELECTEDLOCATION) && !columnName.equals(LOCATION_ID) && !columnName.equals(QTY) && !columnName.equals(NXT) && !columnName.equals(JOBID) && !columnName.equals(DATETIME) && !columnName.equals(UPLOADSTATUS)) {
                            DatabaseModel databaseModel = new DatabaseModel();
                            if (columnName.equals(READMODE)) {
                                if (columnValue.equals("SingleMode")) {
                                    databaseModel.setField_name(columnName);
                                    databaseModel.setField_value("1");
                                } else if (columnValue.equals("MultiMode")) {
                                    databaseModel.setField_name(columnName);
                                    databaseModel.setField_value("2");
                                } else {
                                    databaseModel.setField_name(columnName);
                                    databaseModel.setField_value("3");
                                }
                            } else if (columnName.equals(LOCATION)) {
                                databaseModel.setField_name(columnName);
                                databaseModel.setField_value(c.getString(c.getColumnIndex(LOCATION_ID)));
                            } else {
                                databaseModel.setField_name(columnName);
                                databaseModel.setField_value(columnValue);
                            }
                            databaseModelArrayList.add(databaseModel);
                        }
                    }
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.d("checkException", "getAllItemsDataNew: " + e.getLocalizedMessage());
        } finally {
            try {
                c.close();
            } catch (Exception ignore) {
            }
        }
        return databaseModelArrayList;
    }

    public boolean updateSendData(int starting, int ending) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
//            Log.d("checkBarCode", "inserItemData: " + itemDummyModels.get(i).barCode);
        ContentValues contentValues = new ContentValues();
        contentValues.put(UPLOADSTATUS, "completed");

        try {
//            result = db.insert(ITEM_TABLE, null, contentValues);
            result = db.update(ITEM_TABLE, contentValues, "" + ITEM_ID + " IN (SELECT ITEMID FROM item_table limit " + starting + "," + ending + ")", null);
        } catch (Exception e) {
            Log.d("checkException", "insertUserData: " + e.getLocalizedMessage());
        }

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }
}
