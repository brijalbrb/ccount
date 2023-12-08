package com.y.ccount;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.y.ccount.apicallback.SendCallBack;
import com.y.ccount.helper.DatabaseHelper;
import com.y.ccount.interfaces.ApiInterface;
import com.y.ccount.model.DatabaseModel;
import com.y.ccount.model.ItemData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {
    @BindView(R.id.tvSingleCount)
    TextView tvSingleCount;
    @BindView(R.id.tvMultiCount)
    TextView tvMultiCount;
    @BindView(R.id.tvBlindCount)
    TextView tvBlindCount;

    DatabaseHelper myDb;
    private int singleCount, multiCount, blindCount;
    private ArrayList<DatabaseModel> itemDataArrayList;
    private int currentCount;
    private Dialog dialog = null;
    public static final String MyPREFERENCES = "MyPrefs";
    private int starting, ending;
    private ArrayList<JSONObject> jsonObjectArrayList;
    private JSONArray jsonArray;
    private JSONArray fixedJsonArray;
    private JSONObject finalJsonObject;
    private int totalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        myDb = new DatabaseHelper(this);
        init();
    }

    private void init() {
        currentCount = 0;
        totalCount = 0;
        starting = 0;
        ending = 50;
        getCountData();
//        restoreDatabase();

    }

    private void getCountData() {
        singleCount = myDb.getCount(0);
        multiCount = myDb.getCount(1);
        blindCount = myDb.getCount(2);
        if (singleCount > 0) {
            tvSingleCount.setVisibility(View.VISIBLE);
            tvSingleCount.setText(String.valueOf(singleCount));
        } else {
            tvSingleCount.setVisibility(View.GONE);
        }
        if (multiCount > 0) {
            tvMultiCount.setVisibility(View.VISIBLE);
            tvMultiCount.setText(String.valueOf(multiCount));
        } else {
            tvMultiCount.setVisibility(View.GONE);
        }
        if (blindCount > 0) {
            tvBlindCount.setVisibility(View.VISIBLE);
            tvBlindCount.setText(String.valueOf(blindCount));
        } else {
            tvBlindCount.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        starting = 0;
        ending = 50;
        getCountData();
        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    @OnClick(R.id.clLogOut)
    public void logOutClick() {
        SharedPreferences preferences1 = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor1 = preferences1.edit();
        editor1.putBoolean("isLogin", false);
        editor1.apply();
        startActivity(new Intent(DashboardActivity.this, MainActivity.class));
    }

    @OnClick(R.id.cvSingleMode)
    public void singleMode() {
        startActivity(new Intent(DashboardActivity.this, RegistrationActivity.class).putExtra("mode", "SingleMode")
                .putExtra("from", "Activity").putExtra("value", 0));
    }

    @OnClick(R.id.cvMultiMode)
    public void multiMode() {
        startActivity(new Intent(DashboardActivity.this, RegistrationActivity.class).putExtra("mode", "MultiMode")
                .putExtra("from", "Activity").putExtra("value", 0));

    }

    @OnClick(R.id.cvBlindMode)
    public void blindMode() {
        startActivity(new Intent(DashboardActivity.this, RegistrationActivity.class).putExtra("mode", "BlindMode")
                .putExtra("from", "Activity").putExtra("value", 0));
    }

    @OnClick(R.id.cvUpload)
    public void uploadToDatabase() {
        boolean checkConnection = isOnline();
        if (checkConnection) {
            if (currentCount == 0) {
                dialog = ProgressDialog.show(DashboardActivity.this, "", "Please Wait Uploading Data To Server");
                restoreDatabase();
            }

            if (totalCount == 0) {
                totalCount = myDb.getTotalNumberOfRecords();
                if (totalCount < 50) {
                    itemDataArrayList = myDb.getAllItemsDataNew(starting, ending);
                } else {
                    itemDataArrayList = myDb.getAllItemsDataNew(starting, ending);
                }
            } else {
                itemDataArrayList = myDb.getAllItemsDataNew(starting, ending);
            }
            int startingIndex = 0;
            int endingIndex = 12;
            int finalIndex = startingIndex + endingIndex;
            if (itemDataArrayList != null && itemDataArrayList.size() > 0) {
                jsonObjectArrayList = new ArrayList<>();
                jsonArray = new JSONArray();
                List<DatabaseModel> subArrayList = itemDataArrayList.subList(startingIndex, finalIndex);
                convertToJsonObject(subArrayList, startingIndex, endingIndex, finalIndex, itemDataArrayList);
            } else {
                int finalCount = myDb.getALLNumberOfRecords();
                if (ending < finalCount) {
                    currentCount = 1;
                    starting = ending;
                    starting = ending;
                    ending = ending + 50;
                    uploadToDatabase();
                } else {
                    dialog.dismiss();
                    boolean updateData = myDb.updateSendData(starting, ending);
                    if (updateData) {
                        Log.d("checkData", "onResponse: updated from else " + starting + " " + ending);
                        getCountData();
                    }
                    Toast.makeText(DashboardActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
            }

   /*         String readmode = "";
            if(itemDataArrayList!=null&&itemDataArrayList.size()>0){
                convertToJsonObject(itemDataArrayList);
            }
            if (itemDataArrayList.size() > 0 && itemDataArrayList != null) {
                if (currentCount == 0) {
                    restoreDatabase();
                }
                if (itemDataArrayList.get(currentCount).getReadMode().equals("SingleMode")) {
                    readmode = "1";
                } else if (itemDataArrayList.get(currentCount).getReadMode().equals("MultiMode")) {
                    readmode = "50";
                } else {
                    readmode = "3";
                }
                String location = "";
                if (itemDataArrayList.get(currentCount).getLocationID().equals("")) {
                    location = itemDataArrayList.get(currentCount).getLocation();
                } else {
                    location = itemDataArrayList.get(currentCount).getLocationID();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());
                if (itemDataArrayList != null && itemDataArrayList.size() > 0) {
                    ApiInterface apiInterface = MyApp.retrofit.create(ApiInterface.class);
                    Call<SendCallBack> sendCallBackCall = apiInterface.sendData(itemDataArrayList.get(currentCount).getItemid(),
                            itemDataArrayList.get(currentCount).getBarcode(),
                            itemDataArrayList.get(currentCount).getRecqty(),
                            itemDataArrayList.get(currentCount).getUser_id(),
                            location,
                            itemDataArrayList.get(currentCount).getZone(),
                            itemDataArrayList.get(currentCount).getBin(),
                            readmode,
                            itemDataArrayList.get(currentCount).getReadDateTime(),
                            itemDataArrayList.get(currentCount).getSeqID(),
                            currentDateandTime,
                            itemDataArrayList.get(currentCount).getMou(),
                            itemDataArrayList.get(currentCount).getDes());
                    Log.d("checkData", "uploadToDatabase: " + itemDataArrayList.get(currentCount).getLocationID());
                    Log.d("checkData", "uploadToDatabase: " + readmode);
                    sendCallBackCall.enqueue(new Callback<SendCallBack>() {
                        @Override
                        public void onResponse(Call<SendCallBack> call, Response<SendCallBack> response) {
                            if (response.body().getStatus() == 1) {
                                currentCount = currentCount + 1;
                                if (!(currentCount >= itemDataArrayList.size())) {
                                    uploadToDatabase();
                                } else {
                                    boolean updateData = myDb.updatePendingData();
                                    if (updateData) {
                                        currentCount = 0;
                                        getCountData();
                                        dialog.dismiss();
                                        Toast.makeText(DashboardActivity.this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                dialog.dismiss();
                                Toast.makeText(DashboardActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SendCallBack> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(DashboardActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                dialog.dismiss();
                Toast.makeText(this, "Data Already Uploaded", Toast.LENGTH_SHORT).show();
            }*/
        } else {
            restoreDatabase();
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void convertToJsonObject(List<DatabaseModel> subArrayList, int startingIndex, int endingIndex, int finalIndex, ArrayList<DatabaseModel> itemDataArrayList) {
        try {


            JSONObject projectObj = new JSONObject();
            for (int i = 0; i < subArrayList.size(); i++) {
                String a = subArrayList.get(i).getField_name();
                String b = subArrayList.get(i).getField_value();
                projectObj.put(a, b);

            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());
            projectObj.put("snddt", currentDateandTime);
            jsonArray.put(projectObj);
//            jsonObjectArrayList.add(productValueObject);
            startingIndex = finalIndex;
            endingIndex = 12;
            finalIndex = startingIndex + endingIndex;
            if (!(finalIndex > itemDataArrayList.size())) {
                List<DatabaseModel> subNewArrayList = itemDataArrayList.subList(startingIndex, finalIndex);
                convertToJsonObject(subNewArrayList, startingIndex, endingIndex, finalIndex, itemDataArrayList);
            } else {
                finalJsonObject = new JSONObject();
                finalJsonObject.put("data", jsonArray);
                callSendApi();
                Log.d("checkJson", "convertToJsonObject:finish " + finalJsonObject);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callSendApi() {
        ApiInterface apiInterface = MyApp.retrofit.create(ApiInterface.class);
        Call<SendCallBack> sendCallBackCall = apiInterface.sendData(finalJsonObject);
        sendCallBackCall.enqueue(new Callback<SendCallBack>() {
            @Override
            public void onResponse(Call<SendCallBack> call, Response<SendCallBack> response) {
                if (response.body().getStatus() == 1) {
                    currentCount = 1;
                    if (totalCount > 0) {
                        boolean updateData = myDb.updateSendData(starting, ending);
                        if (updateData) {
                            Log.d("checkData", "onResponse: updated " + starting + " " + ending);
                            starting = ending;
                            ending = ending + 50;
                            uploadToDatabase();
                        }

                    } else {
                        dialog.dismiss();
                        getCountData();
                       /* boolean updateData = myDb.updatePendingData();
                        if (updateData) {
                            getCountData();
                        }*/
                        Toast.makeText(DashboardActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(DashboardActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SendCallBack> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(DashboardActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

    void restoreDatabase() {
      /*  try {
            final String inFileName = "/data/data/" + getPackageName() + "/databases/Ccount.db";
            final String outFileName = "/data/data/com.y.ccount/CcountSend.db";
            File dbFile = new File(inFileName);
            shareFile(dbFile);
            FileInputStream fis = new FileInputStream(dbFile);

            Log.d("checkDatabase", "copyDatabase: outFile = " + outFileName);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            File sendFile = new File(outFileName);
            shareFile(sendFile);
            output.flush();
            output.close();
            fis.close();
        } catch (Exception e) {
            Log.d("checkDatabase", "copyDatabase: backup error");
        }*/
        final String inFileName = "/data/data/" + getPackageName() + "/databases/Ccount.db";
        File file = new File(inFileName);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(inFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (file.exists() && file.canRead()) {
            try {
                String rootPath = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/CcountDatabase/";
                File root = new File(rootPath);
                if (!root.exists()) {
                    root.mkdirs();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String currentDateandTime = sdf.format(new Date());
                String databaseName = "Ccount" + currentDateandTime + ".db";
                File f = new File(rootPath + databaseName);
                if (f.exists()) {
                    f.delete();
                }
                f.createNewFile();

                FileOutputStream out = new FileOutputStream(f);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                out.flush();
                out.close();
                Toast.makeText(this, "Database Successfully Stored At " + rootPath + databaseName, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            try {
//
//                //We need to make a local copy of the file to SDCard, so Gmail can use it
//                File destination = new File(Environment.getExternalStorageDirectory(), "database_copy.db");
//                this.copy(file, destination);
//
//                //Attach file and send
//                Uri uri = Uri.fromFile(destination);
//                Intent email = new Intent(Intent.ACTION_SEND);
//                email.setType("*/*");
//                email.putExtra(android.content.Intent.EXTRA_EMAIL, "sejpalpavan@gmail.com");
//                email.putExtra(android.content.Intent.EXTRA_SUBJECT, "database");
//                email.putExtra(Intent.EXTRA_STREAM, uri);
//                startActivity(Intent.createChooser(email, "Email DB File"));
//            } catch (IOException ioe) {
//                Log.d("checkException", "restoreDatabase: " + ioe.getLocalizedMessage());
//            }
        }


//File copy routine


    }

    private void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
  /*  private void shareFile(File file) {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@domain.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        i.putExtra(Intent.EXTRA_TEXT, "Some message text");
        i.setType("application/octet-stream");
        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(Intent.createChooser(i, "Send e-mail"));
    }*/

    /*@OnClick(R.id.clShare)
    public void sendDatbaseClick() {
        restoreDatabase();
    }*/


}


