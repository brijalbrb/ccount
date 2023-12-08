package com.y.ccount;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.jacksonandroidnetworking.JacksonParserFactory;

import java.io.IOException;
import java.util.ArrayList;

import com.jaredrummler.android.device.DeviceName;
import com.y.ccount.apicallback.ItemCallBack;
import com.y.ccount.apicallback.LocationCallBack;
import com.y.ccount.apicallback.LoginCallBack;
import com.y.ccount.apicallback.SendCallBack;
import com.y.ccount.apicallback.UserCallBack;
import com.y.ccount.helper.DatabaseHelper;
import com.y.ccount.interfaces.ApiInterface;
import com.y.ccount.model.ItemDummyModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //    TextView tvLogin;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    //    @BindView(R.id.tvSync)
//    TextView tvSync;
//    @BindView(R.id.tvClear)
//    TextView tvClear;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etEmail)
    EditText etEmail;
    //    @BindView(R.id.tvSend)
//    TextView tvSend;
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    DatabaseHelper myDb;
    Dialog dialog = null;
    private boolean isLogin;
    private String userID;
    int passID = 0;
    private String model, manufacturer;
    private ArrayList<ItemDummyModel> itemDummyModels;
    private ArrayList<ItemDummyModel> getAllItemDataList;
    public static final String path = Environment.getExternalStorageDirectory() + "/Android/data/com.said.finance/SaidFinance/";
    private String[] barCode = {"8679912077", "671860013624", "8901072002478"};
    public static final String MyPREFERENCES = "MyPrefs";
    private int number = 1;
    private boolean checkLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        checkLogin = preferences.getBoolean("isLogin", false);


        if (checkLogin) {
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
        } else {
            /*DeviceName.with(MainActivity.this).request(new DeviceName.Callback() {

                @Override
                public void onFinished(DeviceName.DeviceInfo info, Exception error) {
                    manufacturer = info.manufacturer;  // "Samsung"
                    String name = info.marketName;            // "Galaxy S8+"
                    model = info.model;                // "SM-G955W"
                    String codename = info.codename;          // "dream2qltecan"
                    String deviceName = info.getName();
                    if (!manufacturer.equals("HMD Global") && !model.equals("TA-1053")) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
//            mClss = clss;
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
                        }
                    }
                }
            });*/
            itemDummyModels = new ArrayList<>();
            getAllItemDataList = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
//            mClss = clss;
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
            }
            myDb = new DatabaseHelper(this);
            ButterKnife.bind(this);

            AndroidNetworking.setParserFactory(new JacksonParserFactory());
            AndroidNetworking.initialize(getApplicationContext());
            init();
        }
    }

    private void init() {
        setClickListeners();
//        getPreferences();
        loadDummyData();

//        tvLogin = findViewById(R.id.tvLogin);
//        tvLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
//            }
//        });
    }

    private void getPreferences() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        userID = sharedpreferences.getString("userId", "null");
    }

    private void loadDummyData() {
        for (int i = 0; i < 3; i++) {
            ItemDummyModel itemDummyModel = new ItemDummyModel();
//            getPreferences();
            itemDummyModel.userId = "2";
            itemDummyModel.itemID = "3";
            itemDummyModel.zone = "rajkot";
            itemDummyModel.barCode = barCode[i];
            itemDummyModel.bin = "5";
            itemDummyModel.qty = "0";
            itemDummyModel.resQty = "0";
            itemDummyModel.location = "";
            itemDummyModel.dateTime = "2018-06-06 14:53:06";
            itemDummyModel.jobId = "201805261942";
            itemDummyModel.nxt = "4";
            itemDummyModel.status = "1";
            itemDummyModel.mou = "5";
            itemDummyModels.add(itemDummyModel);
        }
    }

    private void setClickListeners() {
        tvLogin.setOnClickListener(this);
//        tvSync.setOnClickListener(this);
//        tvClear.setOnClickListener(this);
//        tvSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLogin:
                boolean isInternet = isOnline();
                if (isInternet) {
                    if (isInternet) {
//                        Toast.makeText(this, "internet available", Toast.LENGTH_SHORT).show();
                        callLoginApi();
                    }
                } else {
                    boolean checkUser = myDb.checkUser(MainActivity.this, etEmail.getText().toString(), etPassword.getText().toString());
                    if (checkUser) {
                        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "No User Found", Toast.LENGTH_SHORT).show();
                    }
//                    Toast.makeText(this, "internet is not available", Toast.LENGTH_SHORT).show();
                }
               /* isLogin = myDb.checkUser(MainActivity.this, etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
                if (isLogin) {
                    boolean isAdded = false;
                    SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    isAdded = preferences.getBoolean("databaseAdded", false);
                    userID = preferences.getString("userId", "NULL");
                    if (!isAdded) {
                        dialog = ProgressDialog.show(MainActivity.this, "", "Please wait loading data from server it take some time..");
                        dialog.show();
                        callItemApi("L01", 1);
                    } else {
                        SharedPreferences sharedpreferencess = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                        SharedPreferences.Editor editors = sharedpreferencess.edit();
                        editors.putInt("recQty", 0);
                        editors.putBoolean("isSingleMode", false);
                        editors.putBoolean("isMultiMode", false);
                        editors.putBoolean("isBlindMode", false);
                        editors.putBoolean("isLogin", true);
                        editors.apply();
                        startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
                    }
                } else {
                    Toast.makeText(this, "No User Found", Toast.LENGTH_SHORT).show();
                }*/
//                break;
          /*  case R.id.tvSync:
                SharedPreferences preferencesNew = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                boolean checkData = preferencesNew.getBoolean("allDataLoaded", false);
                if (checkData) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("Confirm Reload...");

                    // Setting Dialog Message
                    alertDialog.setMessage("Database already loaded, do you want to load again?");

                    // Setting Icon to Dialog
//                    alertDialog.setIcon(R.drawable.delete);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("databaseAdded", false);
                            editor.apply();
                            boolean isDelete = myDb.deleteData();
                            if (isDelete) {
                                callLocationApi();
                            }
                            // Write your code here to invoke YES event
//                            Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
//                            Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    //Uncomment the below code to Set the message and title from the strings.xml file
//                    //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);
//
//                    //Setting message manually and performing action on button click
//                    builder.setMessage("Database already loaded,do you want to load again?")
//                            .setCancelable(false)
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                                    editor.putBoolean("databaseAdded", false);
//                                    editor.apply();
//                                    callLocationApi();
//                                    finish();
//                                }
//                            })
//                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    //  Action for 'NO' Button
//                                    dialog.cancel();
//                                }
//                            });
//
//                    //Creating dialog box
//                    AlertDialog alert = builder.create();
//                    //Setting the title manually
//                    alert.setTitle("Warning");
//                    alert.show();
                } else {
                    callLocationApi();
                }
                break;*/
//            case R.id.tvClear:
//                etEmail.setText("");
//                etPassword.setText("");
//                break;
            /*case R.id.tvSend:
                dialog = ProgressDialog.show(MainActivity.this, "", "Please wait...");
                dialog.show();
                callSendApi();
                break;*/
        }
    }

    private void callLoginApi() {
        final Dialog dialog = ProgressDialog.show(MainActivity.this, "", "please wait...");
        ApiInterface apiInterface = MyApp.retrofit.create(ApiInterface.class);
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        Call<LoginCallBack> loginCallBackCall = apiInterface.callLogin(email, password);
        loginCallBackCall.enqueue(new Callback<LoginCallBack>() {
            @Override
            public void onResponse(Call<LoginCallBack> call, Response<LoginCallBack> response) {
                if (response.body().getStatus() != null) {
                    if (response.body().getStatus() == 0) {
                        dialog.dismiss();

                        Toast.makeText(MainActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialog.dismiss();
                    SharedPreferences preferences = getSharedPreferences("loginData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("userID", response.body().getPosts().get(0).getPost().getId());
                    editor.apply();
                    SharedPreferences preferences1 = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = preferences1.edit();
                    editor1.putBoolean("isLogin", true);
                    editor1.apply();
//                    checkLogin = preferences1.getBoolean("isLogin", false);

                    callLocationApi(response.body().getPosts().get(0).getPost().getId());

                }
            }

            @Override
            public void onFailure(Call<LoginCallBack> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callSendApi() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String date = sharedPreferences.getString("date", "null");
        String readmode = sharedPreferences.getString("readMode", "null");
        if (getAllItemDataList.size() == 0) {
            getAllItemDataList = myDb.getAllItemData();
        }
        if (getAllItemDataList != null && getAllItemDataList.size() > 0) {
            ApiInterface apiInterface = MyApp.retrofit.create(ApiInterface.class);
            Call<SendCallBack> loginCallBackCall = apiInterface.send(getAllItemDataList.get(passID).itemID,
                    getAllItemDataList.get(passID).barCode,
                    getAllItemDataList.get(passID).resQty,
                    getAllItemDataList.get(passID).userId,
                    getAllItemDataList.get(passID).location,
                    getAllItemDataList.get(passID).zone,
                    getAllItemDataList.get(passID).bin,
                    readmode,
                    date,
                    String.valueOf(0),
                    String.valueOf(0),
                    getAllItemDataList.get(passID).mou);

            loginCallBackCall.enqueue(new Callback<SendCallBack>() {
                @Override
                public void onResponse(Call<SendCallBack> call, Response<SendCallBack> response) {
                    if (passID <= getAllItemDataList.size() - 1) {
                        if (passID != getAllItemDataList.size() - 1) {
                            passID++;
                            callSendApi();
                            Toast.makeText(MainActivity.this, "calling again", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "Send Successfully", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Send Successfully", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SendCallBack> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            dialog.dismiss();
            Toast.makeText(this, "No item data found", Toast.LENGTH_SHORT).show();

        }
    }


    private void callLocationApi(final String userID) {
        boolean checkData = myDb.checkItemData();
        if (checkData) {
            showAlertDialogue(userID);
        } else {
            dialog = ProgressDialog.show(MainActivity.this, "", "Please wait...");
            dialog.show();
            ApiInterface apiInterface = MyApp.retrofit.create(ApiInterface.class);
            Call<LocationCallBack> loginCallBackCall = apiInterface.location(Integer.valueOf(userID), "json");
            loginCallBackCall.enqueue(new Callback<LocationCallBack>() {
                @Override
                public void onResponse(Call<LocationCallBack> call, Response<LocationCallBack> response) {
//                dialog.dismiss();
                    boolean isInserted = false;
                    if(response.body()!=null)
                    {
                        if (response.body().getPosts() != null)
                        {
                            for (int i = 0; i < response.body().getPosts().size(); i++)
                            {
                                isInserted = myDb.inserData(i, response);
                            }
                        }
                    }
                    callUserApi(userID);
//                Toast.makeText(MainActivity.this, "login succesfull" + response.body().getPosts().get(0).getPost().getLocationdec(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<LocationCallBack> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void showAlertDialogue(final String userID) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Saved Data Already There,Do You Want Clear Data?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int arg1) {
                        boolean deleteData = myDb.deleteData();
                        if (deleteData) {
                            dialog = ProgressDialog.show(MainActivity.this, "", "Please wait...");
                            dialog.show();
                            ApiInterface apiInterface = MyApp.retrofit.create(ApiInterface.class);
                            Call<LocationCallBack> loginCallBackCall = apiInterface.location(Integer.valueOf(userID), "json");
                            loginCallBackCall.enqueue(new Callback<LocationCallBack>() {
                                @Override
                                public void onResponse(Call<LocationCallBack> call, Response<LocationCallBack> response) {
//                dialog.dismiss();
                                    boolean isInserted = false;
                                    for (int i = 0; i < response.body().getPosts().size(); i++) {
                                        isInserted = myDb.inserData(i, response);
                                    }

                                    if (isInserted == true) {
//                    Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
//                    Toast.makeText(MainActivity.this, "LocationDataLoaded", Toast.LENGTH_SHORT).show();
                                        callUserApi(userID);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                                    }
//                Toast.makeText(MainActivity.this, "login succesfull" + response.body().getPosts().get(0).getPost().getLocationdec(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onFailure(Call<LocationCallBack> call, Throwable t) {
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, DashboardActivity.class));
//                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
//    }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZBAR_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if(mClss != null) {
//                        Intent intent = new Intent(this, mClss);
//                        startActivity(intent);
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
//            mClss = clss;
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ZBAR_CAMERA_PERMISSION);
                    }
//                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    private void callUserApi(final String userID) {
        boolean checkUserData = myDb.checkUserData();
        if (checkUserData) {
            boolean deletedata = myDb.deleteUserData();
        }
        ApiInterface apiInterface = MyApp.retrofit.create(ApiInterface.class);
        Call<UserCallBack> userCallBackCall = apiInterface.user();
        userCallBackCall.enqueue(new Callback<UserCallBack>() {
            @Override
            public void onResponse(Call<UserCallBack> call, Response<UserCallBack> response) {
                boolean isInserted = false;
//                Toast.makeText(MainActivity.this, "size" + response.body().getPosts().size(), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < response.body().getPosts().size(); i++) {
                    isInserted = myDb.insertUserData(i, response);
                }
                if (isInserted == true) {
//                    dialog.dismiss();
//                    Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editors = preferences.edit();
                    editors.putBoolean("allDataLoaded", true);
                    editors.apply();
//                    Toast.makeText(MainActivity.this, "userDataLoaded", Toast.LENGTH_SHORT).show();
                    callItemApi("1", Integer.valueOf(userID));

//                    callItemApi("L01", 1);
                } else {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserCallBack> call, Throwable t) {

            }
        });

    }

    private void callItemApi(String text, int userId) {
        ApiInterface apiInterface = MyApp.retrofit.create(ApiInterface.class);
        Call<ItemCallBack> itemCallBackCall = apiInterface.item(Integer.parseInt(text), userId);
        itemCallBackCall.enqueue(new Callback<ItemCallBack>() {
            @Override
            public void onResponse(Call<ItemCallBack> call, Response<ItemCallBack> response) {
                boolean isInserted = false;
                for (int i = 0; i < response.body().getPosts().size(); i++) {
                    isInserted = myDb.inserItemData(i, response);
                }
                if (isInserted == true) {
                    dialog.dismiss();
                    startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                    finish();
//                    Toast.makeText(MainActivity.this, "item data loaded successfully", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                }
/*
//                dialog.dismiss();
                boolean isInserted = false;
//                for (int i = 0; i < response.body().getPosts().size(); i++) {
//                    isInserted = myDb.insertUserData(i, response);
//                }
                isInserted = myDb.insertNewItemData(userID, "1", "1", response);
                int number = Integer.parseInt(response.body().getPosts().get(0).getPost().getNxt());
                if (isInserted == true) {
//                    Log.d("checkFinal", "onResponse: ");
//                    dialog.dismiss();
//                    Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    if (number != 0) {
//                        number = number + 1;

                        Log.d("checkData", "onResponse:inserted " + number);
//                    Toast.makeText(MainActivity.this, "data loaded" + number, Toast.LENGTH_SHORT).show();
                        callItemApi("L01", number);
                    } else {
                        dialog.dismiss();
                        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("databaseAdded", true);
                        editor.putInt("recQty", 0);
                        editor.putBoolean("isSingleMode", false);
                        editor.putBoolean("isMultiMode", false);
                        editor.putBoolean("isBlindMode", false);
                        editor.putBoolean("isLogin", true);
                        editor.apply();
                        Toast.makeText(MainActivity.this, "all database added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                }
*/

            }

            @Override
            public void onFailure(Call<ItemCallBack> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();

            }
        });
//        boolean isInserted = false;
//        isInserted = myDb.inserItemData(itemDummyModels);
//        if (isInserted) {
//            Toast.makeText(this, "All Data Loaded Succesfully", Toast.LENGTH_SHORT).show();
//            SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean("databaseAdded", true);
//            editor.putInt("recQty", 0);
//            editor.putBoolean("isSingleMode", false);
//            editor.putBoolean("isMultiMode", false);
//            editor.putBoolean("isBlindMode", false);
//            editor.putBoolean("isLogin", true);
//            editor.apply();
//            startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
//        }

    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    @OnClick(R.id.tvClearDatabase)
    public void clearDatabaseClick() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do You Want To Clear Data?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int arg1) {
                        boolean deleteData = myDb.deleteData();
                        if (deleteData) {
                            Toast.makeText(MainActivity.this, "Database Clear Successfully", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

//                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

       /* boolean checkData = myDb.checkItemData();
        if (checkData) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("here is data already saved do you want clear?");
            alertDialogBuilder.setPositiveButton("yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            Toast.makeText(MainActivity.this, "You clicked yes button", Toast.LENGTH_LONG).show();
                        }
                    });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();*/
//    }
    }

//    private void getData() {
//        Cursor res = myDb.getAllData();
//        if (res.getCount() == 0) {
//            // show message
//            Toast.makeText(this, "Nothing Found", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        StringBuffer buffer = new StringBuffer();
//        while (res.moveToNext()) {
//            buffer.append("Id :" + res.getString(0) + "\n");
//            buffer.append("Location :" + res.getString(1) + "\n");
//            buffer.append("Desc :" + res.getString(2) + "\n");
////            buffer.append("Marks :"+ res.getString(3)+"\n\n");
//        }
//        Log.d("checkData", "getData: " + buffer.toString());
//        Toast.makeText(this, "data is" + buffer.toString(), Toast.LENGTH_SHORT).show();
//    }

    /**
     * Returns the consumer friendly device name
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }


}
