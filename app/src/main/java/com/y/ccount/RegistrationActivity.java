package com.y.ccount;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.y.ccount.helper.DatabaseHelper;
import com.y.ccount.model.ItemData;
import com.y.ccount.model.ItemDummyModel;
import com.y.ccount.model.LocationModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends AppCompatActivity {
    //    TextView tvReadMode;
    private String barCode;
    Dialog dialog = null;
    DatabaseHelper myDb;
    public static boolean isSingleMode, isMultiMode, isBlindMode;
    private ArrayList<ItemDummyModel> itemDummyModelArrayList;
    @BindView(R.id.etBarCode)
    EditText etBarCode;
    @BindView(R.id.tvClear)
    TextView tvClear;
    @BindView(R.id.tvSave)
    TextView tvSave;
    private int value;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.clMain)
    ConstraintLayout clMain;
    @BindView(R.id.etDescriptionNew)
    EditText etDescriptionNew;
    @BindView(R.id.tvSaveNew)
    Button tvSaveNew;
    @BindView(R.id.tvClearNew)
    Button tvClearNew;
    //    @BindView(R.id.tvScan)
//    TextView tvScan;
//    @BindView(R.id.tvLogout)
//    TextView tvLogout;
    private int recQty = 1, recNewQty = 0;
    @BindView(R.id.etQty)
    EditText etQty;
    @BindView(R.id.etMou)
    EditText etMou;
    @BindView(R.id.etDescription)
    EditText etDescription;
    @BindView(R.id.etSize)
    EditText etSize;
    @BindView(R.id.spLocation)
    Spinner spLocation;
    @BindView(R.id.ivFooter)
    ImageView ivFooter;
    private String mode, from;
    private boolean isDateSelected;
    private ArrayList<ItemData> itemDataArrayList;
    private List<String> postDataArrayList;
    private List<LocationModel> locationModelList;
    private int selectedPosition;

    public static final String MyPREFERENCES = "MyPrefs";
    private boolean isNextClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        myDb = new DatabaseHelper(this);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        isNextClick = false;
        itemDummyModelArrayList = new ArrayList<>();
        postDataArrayList = new ArrayList<>();
        locationModelList = new ArrayList<>();
        barCode = getIntent().getStringExtra("barcode");
        mode = getIntent().getStringExtra("mode");
        from = getIntent().getStringExtra("from");
        value = getIntent().getIntExtra("value", -1);
        tvTitle.setText(mode);
        if (mode.equals("MultiMode")) {
            etQty.setClickable(false);
            etQty.setFocusable(false);
            etQty.setFocusableInTouchMode(false);
        } else {
            etQty.setClickable(true);
            etQty.setFocusable(true);
            etQty.setFocusableInTouchMode(true);
        }
//        postDataArrayList = myDb.getLocationData();
        locationModelList = myDb.getLocationData();
        for (int i = 0; i < locationModelList.size(); i++) {
            postDataArrayList.add(locationModelList.get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, postDataArrayList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLocation.setAdapter(adapter);
        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (barCode != null) {
            etBarCode.setText(barCode);
            if (!mode.equals("BlindMode")) {
                checkInDatabase();
            }
        }
        etBarCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!mode.equals("BlindMode")) {
                        if (barCode == null) {
                            if (!isNextClick) {
                                checkInDatabase();
                            }
                        }

                    }
//                    Toast.makeText(RegistrationActivity.this, "lost focus", Toast.LENGTH_SHORT).show();
                    // code to execute when EditText loses focus
                } else {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(etBarCode.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                    etBarCode.requestFocus();
                }
            }
        });
        etBarCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (mode.equals("MultiMode")) {
                        isNextClick = true;
                        checkInDatabase();
                    }
                    return true;
                }
                return false;
            }
        });
//        SharedPreferences sharedpreferencess = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//        SharedPreferences.Editor editors = sharedpreferencess.edit();
//        editors.putInt("recQty", recNewQty);
//        editors.apply();

//        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//        recNewQty = preferences.getInt("recQty", 0);
//        if (recNewQty == 0) {
//            etQty.setText("");
//        }
//        etQty.setText("");

//        Toast.makeText(this, "barcode " + barCode, Toast.LENGTH_LONG).show();
//        barCode = "671860013624";

           /* dialog = ProgressDialog.show(RegistrationActivity.this, "", "Please wait...");
            dialog.show();
            itemDummyModelArrayList = myDb.getItemData(barCode);
            if (itemDummyModelArrayList != null && itemDummyModelArrayList.size() > 0) {
                dialog.dismiss();
                etEmail.setText(itemDummyModelArrayList.get(0).barCode);
                etSize.setText(itemDummyModelArrayList.get(0).bin);
                etDescription.setText(itemDummyModelArrayList.get(0).zone);
                etMou.setText(itemDummyModelArrayList.get(0).mou);
//                etQty.setText(itemDummyModelArrayList.get(0).resQty);
                checkModePreference();
                if (!etQty.getText().toString().equals("") && !isMultiMode) {
                    SharedPreferences sharedpreferences1 = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    recNewQty = sharedpreferences1.getInt("recQty", 0);
                    if (recNewQty != 0) {
                        recQty = recNewQty;
                        etQty.setText(String.valueOf(recNewQty));
                    } else {
                        etQty.setText(String.valueOf(recQty));
                    }
                    recQty++;
                    SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putInt("recQty", recQty);
                    editor.apply();
                }

//                isMultiMode = true;
                if (isMultiMode) {
                    SharedPreferences sharedpreferences1 = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    recNewQty = sharedpreferences1.getInt("recQty", 0);

                    if (recNewQty != 0) {
                        recQty = recNewQty;
                        etQty.setText(String.valueOf(recNewQty));
                    } else {
                        etQty.setText(String.valueOf(recQty));
                    }
                    recQty++;
                    SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putInt("recQty", recQty);
                    editor.apply();
                }
//                Toast.makeText(this, "bar code is " + itemDummyModelArrayList.get(0).barCode, Toast.LENGTH_SHORT).show();
            } else {
                dialog.dismiss();
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
            }*/

//            Toast.makeText(this, "bar code is " + barCode, Toast.LENGTH_SHORT).show();
//        }
//        tvReadMode = findViewById(R.id.tvReadMode);
       /* tvReadMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                final PopupMenu popup = new PopupMenu(RegistrationActivity.this, tvReadMode);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Single Mode")) {
                            isSingleMode = true;
                            isMultiMode = false;
                            isBlindMode = false;
                            SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean("isSingleMode", isSingleMode);
                            editor.putBoolean("isBlindMode", isBlindMode);
                            editor.putBoolean("isMultiMode", isMultiMode);
                            editor.putString("readMode", "singleMode");
//                    editor.putLong("data", Long.parseLong(date));
//                            Toast.makeText(context, "id is" + cursor.getString(cursor.getColumnIndex(LOCATION_ID)), Toast.LENGTH_LONG).show();
                            editor.apply();
                            Toast.makeText(RegistrationActivity.this, item.getTitle() + " selected", Toast.LENGTH_SHORT).show();
                        }
                        if (item.getTitle().equals("Multi Mode")) {
                            isMultiMode = true;
                            isSingleMode = false;
                            isBlindMode = false;
                            SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean("isSingleMode", isSingleMode);
                            editor.putBoolean("isBlindMode", isBlindMode);
                            editor.putBoolean("isMultiMode", isMultiMode);
                            editor.putString("readMode", "MultiMode");

                            if (!etQty.getText().toString().equals("")) {
                                editor.putInt("recQty", Integer.parseInt(etQty.getText().toString().trim()) + 1);
                            }
//                            Toast.makeText(context, "id is" + cursor.getString(cursor.getColumnIndex(LOCATION_ID)), Toast.LENGTH_LONG).show();
                            editor.apply();

                            Toast.makeText(RegistrationActivity.this, item.getTitle() + " selected", Toast.LENGTH_SHORT).show();
                        }
                        if (item.getTitle().equals("Blind Mode")) {
                            isMultiMode = false;
                            isSingleMode = false;
                            isBlindMode = true;
                            SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean("isSingleMode", isSingleMode);
                            editor.putBoolean("isBlindMode", isBlindMode);
                            editor.putBoolean("isMultiMode", isMultiMode);
                            editor.putString("readMode", "BlindMode");
//                            Toast.makeText(context, "id is" + cursor.getString(cursor.getColumnIndex(LOCATION_ID)), Toast.LENGTH_LONG).show();
                            editor.apply();
                            Toast.makeText(RegistrationActivity.this, item.getTitle() + " selected", Toast.LENGTH_SHORT).show();
                        }
                        popup.dismiss();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });*/

       /* tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        checkKeyBoard();
    }

    private void showAlertDialogue() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Record Is Not Found Do You Want To Save?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int arg1) {

                        SharedPreferences preferences = getSharedPreferences("loginData", MODE_PRIVATE);
                        String userId = preferences.getString("userID", "null");
                        String item_id = myDb.getLastItemId();
                        int newItemId = Integer.parseInt(item_id) + 1;
                        String seq_id = myDb.getLastSeqId();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        Log.d("checkDateTime", "updateData: " + currentDateandTime);
                        ItemData itemData = new ItemData();
                        itemData.setUser_id(userId);
                        itemData.setItemid(String.valueOf(newItemId));
                        itemData.setRecqty(etQty.getText().toString());
                        if (seq_id.equals("")) {
                            itemData.setSeqID(String.valueOf("1"));
                        } else {
                            int newSeqId = Integer.parseInt(seq_id) + 1;
                            itemData.setSeqID(String.valueOf(newSeqId));
                        }
                        itemData.setReadMode(mode);
                        itemData.setReadDateTime(currentDateandTime);
                        itemData.setDes(etDescriptionNew.getText().toString());
                        itemData.setMou(etMou.getText().toString());
                        itemData.setQty("");
                        itemData.setZone(etDescription.getText().toString());
                        itemData.setNxt("");
                        itemData.setSelectedLocation(String.valueOf(selectedPosition));
                        itemData.setLocation(spLocation.getSelectedItem().toString());
                        itemData.setLocationID(locationModelList.get(selectedPosition).getId());
                        itemData.setJobid("");
                        itemData.setDatetime("");
                        itemData.setBin(etSize.getText().toString());
                        itemData.setBarcode(etBarCode.getText().toString());
                        itemDataArrayList = new ArrayList<>();
                        itemDataArrayList.add(itemData);
                        boolean isUpdate = myDb.inserItemDataCurrent(itemDataArrayList);
                        if (isUpdate) {
                            itemDataArrayList = new ArrayList<>();
                            Toast.makeText(RegistrationActivity.this, "Successfully Inserted " + etBarCode.getText().toString(), Toast.LENGTH_SHORT).show();
                            etBarCode.setText("");
                            etQty.setText("");
                            etMou.setText("");
                            etDescriptionNew.setText("");
//                etDescription.setText("");
                            etSize.setText("");
                            etBarCode.setClickable(true);
                            etBarCode.setFocusable(true);
                            etBarCode.setFocusableInTouchMode(true);
                            etMou.setClickable(true);
                            etMou.setFocusable(true);
                            etMou.setFocusableInTouchMode(true);
                            etDescription.setClickable(true);
                            etDescription.setFocusable(true);
                            etDescription.setFocusableInTouchMode(true);
                            etSize.setClickable(true);
                            etSize.setFocusable(true);
                            etSize.setFocusableInTouchMode(true);
                            etBarCode.requestFocus();

                        }
                        dialog1.dismiss();
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
//    }
    }

    private boolean checkInDatabase() {
        boolean checkResult = false;
        itemDataArrayList = new ArrayList<>();

        itemDataArrayList = myDb.checkData(etBarCode.getText().toString());
        if (itemDataArrayList != null && itemDataArrayList.size() > 0) {
            checkResult = true;
            etBarCode.setText(itemDataArrayList.get(0).getBarcode());
            if (mode.equals("MultiMode") && from.equals("ScannerActivity")) {
                if (value == 0) {
                    if (!itemDataArrayList.get(0).getRecqty().equals("")) {
                        value = Integer.parseInt(itemDataArrayList.get(0).getRecqty()) + 1;
                    }
                } else {
                    value = value + 1;
                }
                etQty.setText(value + "");
            } else {
                if (isNextClick) {
                    if (value == 0) {
                        if (!itemDataArrayList.get(0).getRecqty().equals("")) {
                            value = Integer.parseInt(itemDataArrayList.get(0).getRecqty()) + 1;
                        }
                    } else {
                        value = value + 1;
                    }
                    etQty.setText(value + "");
                } else {
                    etQty.setText(itemDataArrayList.get(0).getRecqty());
                }
            }
            spLocation.setSelection(Integer.parseInt(itemDataArrayList.get(0).getSelectedLocation()));
            etDescription.setText(itemDataArrayList.get(0).getZone());
            etSize.setText(itemDataArrayList.get(0).getBin());
            etMou.setText(itemDataArrayList.get(0).getMou());
            etDescriptionNew.setText(itemDataArrayList.get(0).getDes());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    etQty.requestFocus();
                    etQty.setFocusable(true);
                    etQty.setFocusableInTouchMode(true);
//                    etBarCode.setClickable(false);
//                    etBarCode.setFocusableInTouchMode(false);
//                    etBarCode.setFocusable(false);
//                    etMou.setClickable(false);
//                    etMou.setFocusableInTouchMode(false);
//                    etMou.setFocusable(false);
//                    etDescription.setFocusableInTouchMode(false);
//                    etDescription.setClickable(false);
//                    etDescription.setFocusable(false);
//                    etSize.setFocusableInTouchMode(false);
//                    etSize.setClickable(false);
//                    etSize.setFocusable(false);
                    if (mode.equals("MultiMode")) {
                        etQty.setClickable(false);
                        etQty.setFocusable(false);
                    } else {
                        etQty.setClickable(true);
                        etQty.setFocusable(true);
                        etQty.setFocusableInTouchMode(true);
                    }

                }
            }, 1000);


        } else {

            checkResult = false;
//            etBarCode.setClickable(true);
//            etBarCode.setFocusable(true);
//            etBarCode.setFocusableInTouchMode(true);
//            etMou.setClickable(true);
//            etMou.setFocusable(true);
//            etMou.setFocusableInTouchMode(true);
//            etDescription.setClickable(true);
//            etDescription.setFocusable(true);
//            etDescription.setFocusableInTouchMode(true);
//            etSize.setClickable(true);
//            etSize.setFocusable(true);
//            etSize.setFocusableInTouchMode(true);
            if (mode.equals("MultiMode")) {
                etQty.setClickable(false);
                etQty.setFocusable(false);
                etQty.setFocusableInTouchMode(false);
            } else {
                etQty.setClickable(true);
                etQty.setFocusable(true);
                etQty.setFocusableInTouchMode(true);
            }
            if (mode.equals("MultiMode")) {
                etQty.setText("1");
            }
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
          /*  new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    etDescriptionNew.setText("");
                    etDescription.setText("");
                    etSize.setText("");
                    etMou.setText("");
                }
            }, 1000);*/
        }
        return checkResult;
    }

    private void checkKeyBoard() {
        clMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                clMain.getWindowVisibleDisplayFrame(r);
                int screenHeight = clMain.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

//                Log.d(TAG, "keypadHeight = " + keypadHeight);

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    ivFooter.setVisibility(View.GONE);
                    tvSaveNew.setVisibility(View.VISIBLE);
                    tvClearNew.setVisibility(View.VISIBLE);
                    Log.d("checkKeyBoard", "onGlobalLayout: open");
                } else {
                    ivFooter.setVisibility(View.VISIBLE);
                    tvSaveNew.setVisibility(View.GONE);
                    tvClearNew.setVisibility(View.GONE);
                    Log.d("checkKeyBoard", "onGlobalLayout: close");

                    // keyboard is closed
                }
            }
        });
    }

    private void updateData() {
        boolean isUpdate = false;
        if (itemDataArrayList != null && itemDataArrayList.size() > 0) {
            isUpdate = myDb.updateItemData(itemDataArrayList);
            if (isUpdate) {
                Toast.makeText(RegistrationActivity.this, "Successfully Updated " + etBarCode.getText().toString(), Toast.LENGTH_SHORT).show();
                KeyListener variable;
                variable = etBarCode.getKeyListener();
                etBarCode.setText("");
                etQty.setText("");
                etMou.setText("");
                etDescriptionNew.setText("");
//                etDescription.setText("");
                etSize.setText("");
                etBarCode.setClickable(true);
                etBarCode.setFocusableInTouchMode(true);
                etBarCode.setFocusable(true);
//                etBarCode.setKeyListener(variable);
                etMou.setClickable(true);
                etMou.setFocusableInTouchMode(true);
                etMou.setFocusable(true);
                etDescription.setClickable(true);
                etDescription.setFocusableInTouchMode(true);
                etDescription.setFocusable(true);
                etSize.setClickable(true);
                etSize.setFocusableInTouchMode(true);
                etSize.setFocusable(true);
                etBarCode.requestFocus();
            } else {
                Toast.makeText(RegistrationActivity.this, "failed to update", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkModePreference() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        isSingleMode = sharedpreferences.getBoolean("isSingleMode", false);
        isMultiMode = sharedpreferences.getBoolean("isMultiMode", false);
        isBlindMode = sharedpreferences.getBoolean("isBlindMode", false);
    }

    @OnClick(R.id.ivScan)
    public void scanClick() {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        startActivity(new Intent(RegistrationActivity.this, FullScannerActivity.class).putExtra("mode", mode)
                .putExtra("value", value));
        finish();
    }

    @OnClick(R.id.ivBack)
    public void backClick() {
        finish();
    }

    @OnClick({R.id.tvSaveNew, R.id.tvSave})
    public void saveClick() {
        if (mode.equals("BlindMode")) {
            if (etBarCode.getText().toString().equals("")) {
                Toast.makeText(RegistrationActivity.this, "Please Enter Bar Code", Toast.LENGTH_SHORT).show();
            } else if (etQty.getText().toString().equals("")) {
                Toast.makeText(RegistrationActivity.this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
            } else if (etDescription.getText().toString().equals("")) {
                Toast.makeText(RegistrationActivity.this, "Please Enter Zone", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences preferences = getSharedPreferences("loginData", MODE_PRIVATE);
                String userId = preferences.getString("userID", "null");
                String item_id = myDb.getLastItemId();
                int newItemId = Integer.parseInt(item_id) + 1;
                String seq_id = myDb.getLastSeqId();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());
                Log.d("checkDateTime", "updateData: " + currentDateandTime);
                ItemData itemData = new ItemData();
                itemData.setUser_id(userId);
                itemData.setItemid(String.valueOf(newItemId));
                itemData.setRecqty(etQty.getText().toString());
                if (seq_id.equals("")) {
                    itemData.setSeqID(String.valueOf("1"));
                } else {
                    int newSeqId = Integer.parseInt(seq_id) + 1;
                    itemData.setSeqID(String.valueOf(newSeqId));
                }
                itemData.setReadMode(mode);
                itemData.setReadDateTime(currentDateandTime);
                itemData.setDes(etDescriptionNew.getText().toString());
                itemData.setMou(etMou.getText().toString());
                itemData.setQty("");
                itemData.setZone(etDescription.getText().toString());
                itemData.setNxt("");
                itemData.setSelectedLocation(String.valueOf(selectedPosition));
                itemData.setLocation(spLocation.getSelectedItem().toString());
                itemData.setLocationID(locationModelList.get(selectedPosition).getId());
                itemData.setJobid("");
                itemData.setDatetime("");
                itemData.setBin(etSize.getText().toString());
                itemData.setBarcode(etBarCode.getText().toString());
                itemDataArrayList = new ArrayList<>();
                itemDataArrayList.add(itemData);
                boolean isUpdate = myDb.inserItemDataCurrent(itemDataArrayList);
                if (isUpdate) {
                    itemDataArrayList = new ArrayList<>();
                    Toast.makeText(RegistrationActivity.this, "Successfully Inserted " + etBarCode.getText().toString(), Toast.LENGTH_SHORT).show();
                    etBarCode.setText("");
                    etQty.setText("");
                    etMou.setText("");
                    etDescriptionNew.setText("");
//                etDescription.setText("");
                    etSize.setText("");
                    etBarCode.setClickable(true);
                    etBarCode.setFocusable(true);
                    etBarCode.setFocusableInTouchMode(true);
                    etMou.setClickable(true);
                    etMou.setFocusable(true);
                    etMou.setFocusableInTouchMode(true);
                    etDescription.setClickable(true);
                    etDescription.setFocusable(true);
                    etDescription.setFocusableInTouchMode(true);
                    etSize.setClickable(true);
                    etSize.setFocusable(true);
                    etSize.setFocusableInTouchMode(true);
                    etBarCode.requestFocus();

                }
            }
        } else {
            if (mode.equals("SingleMode")) {
                if (etBarCode.getText().toString().equals("")) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Bar Code", Toast.LENGTH_SHORT).show();
                } else if (etQty.getText().toString().equals("")) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
                } else if (etDescription.getText().toString().equals("")) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Zone", Toast.LENGTH_SHORT).show();
                } else {
                    if (itemDataArrayList.size() == 0) {
                        boolean result = checkInDatabase();
                        if (result) {
                            if (itemDataArrayList != null && itemDataArrayList.size() > 0) {
                                itemDataArrayList.get(0).setRecqty(etQty.getText().toString());
                                itemDataArrayList.get(0).setBarcode(etBarCode.getText().toString());
                                itemDataArrayList.get(0).setSelectedLocation(String.valueOf(selectedPosition));
                                itemDataArrayList.get(0).setReadMode(mode);
                                itemDataArrayList.get(0).setZone(etDescription.getText().toString());
                                itemDataArrayList.get(0).setBin(etSize.getText().toString());
                                itemDataArrayList.get(0).setMou(etMou.getText().toString());
                                itemDataArrayList.get(0).setLocationID(locationModelList.get(selectedPosition).getId());
                                itemDataArrayList.get(0).setDes(etDescriptionNew.getText().toString());
                            }
                            updateData();
                        } else {
                            showAlertDialogue();
                        }
                    } else {
                        if (itemDataArrayList != null && itemDataArrayList.size() > 0) {
                            itemDataArrayList.get(0).setRecqty(etQty.getText().toString());
                            itemDataArrayList.get(0).setBarcode(etBarCode.getText().toString());
                            itemDataArrayList.get(0).setSelectedLocation(String.valueOf(selectedPosition));
                            itemDataArrayList.get(0).setReadMode(mode);
                            itemDataArrayList.get(0).setZone(etDescription.getText().toString());
                            itemDataArrayList.get(0).setBin(etSize.getText().toString());
                            itemDataArrayList.get(0).setMou(etMou.getText().toString());
                            itemDataArrayList.get(0).setLocationID(locationModelList.get(selectedPosition).getId());
                            itemDataArrayList.get(0).setDes(etDescriptionNew.getText().toString());
                        }
                        updateData();
                    }
                }
                /*}*/ /*else {
                            Toast.makeText(RegistrationActivity.this, "Please Fill Up All Data", Toast.LENGTH_SHORT).show();
                        }*/

            } else {
                if (etBarCode.getText().toString().equals("")) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Bar Code", Toast.LENGTH_SHORT).show();
                } else if (etQty.getText().toString().equals("")) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
                } else if (etDescription.getText().toString().equals("")) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Zone", Toast.LENGTH_SHORT).show();
                } else {
                    if (itemDataArrayList.size() == 0) {
                        boolean result = checkInDatabase();
                        if (result) {
                            if (itemDataArrayList != null && itemDataArrayList.size() > 0) {
                                itemDataArrayList.get(0).setRecqty(etQty.getText().toString());
                                itemDataArrayList.get(0).setBarcode(etBarCode.getText().toString());
                                itemDataArrayList.get(0).setSelectedLocation(String.valueOf(selectedPosition));
                                itemDataArrayList.get(0).setReadMode(mode);
                                itemDataArrayList.get(0).setZone(etDescription.getText().toString());
                                itemDataArrayList.get(0).setBin(etSize.getText().toString());
                                itemDataArrayList.get(0).setMou(etMou.getText().toString());
                                itemDataArrayList.get(0).setLocationID(locationModelList.get(selectedPosition).getId());
                                itemDataArrayList.get(0).setDes(etDescriptionNew.getText().toString());
                            }
                            updateData();
                        } else {
                            showAlertDialogue();
                        }
                    } else {
                        if (itemDataArrayList != null && itemDataArrayList.size() > 0) {
                            itemDataArrayList.get(0).setRecqty(etQty.getText().toString());
                            itemDataArrayList.get(0).setBarcode(etBarCode.getText().toString());
                            itemDataArrayList.get(0).setSelectedLocation(String.valueOf(selectedPosition));
                            itemDataArrayList.get(0).setReadMode(mode);
                            itemDataArrayList.get(0).setZone(etDescription.getText().toString());
                            itemDataArrayList.get(0).setBin(etSize.getText().toString());
                            itemDataArrayList.get(0).setMou(etMou.getText().toString());
                            itemDataArrayList.get(0).setLocationID(locationModelList.get(selectedPosition).getId());
                            itemDataArrayList.get(0).setDes(etDescriptionNew.getText().toString());
                        }
                        updateData();
                    }
                }
                       /* if (etBarCode.getText().toString().equals("")) {
                            Toast.makeText(RegistrationActivity.this, "Please Enter Bar Code", Toast.LENGTH_SHORT).show();
                        } else if (etQty.getText().toString().equals("")) {
                            Toast.makeText(RegistrationActivity.this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
                        } else {
//                            boolean result = checkInDatabase();
//                            if (result) {
                            if (itemDataArrayList != null && itemDataArrayList.size() > 0) {
                                itemDataArrayList.get(0).setRecqty(etQty.getText().toString());
                                itemDataArrayList.get(0).setBarcode(etBarCode.getText().toString());
                                itemDataArrayList.get(0).setSelectedLocation(String.valueOf(selectedPosition));
                                itemDataArrayList.get(0).setReadMode(mode);
                                itemDataArrayList.get(0).setZone(etDescription.getText().toString());
                                itemDataArrayList.get(0).setBin(etSize.getText().toString());
                                itemDataArrayList.get(0).setMou(etMou.getText().toString());
                                itemDataArrayList.get(0).setLocationID(locationModelList.get(selectedPosition).getId());
                                itemDataArrayList.get(0).setDes(etDescriptionNew.getText().toString());
                            }
                            updateData();
                        }*/

            }
        }

    }



    @OnClick({R.id.tvClear, R.id.tvClearNew})
    public void clearClick() {
        etBarCode.setText("");
        etQty.setText("");
        etMou.setText("");
        etDescription.setText("");
        etSize.setText("");
        etDescriptionNew.setText("");
    }
}
