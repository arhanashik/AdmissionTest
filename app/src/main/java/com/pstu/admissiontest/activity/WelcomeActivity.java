package com.pstu.admissiontest.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.pstu.admissiontest.R;
import com.pstu.admissiontest.database.DBHandler;
import com.pstu.admissiontest.model.Item;
import com.pstu.admissiontest.variables.FinalVariables;
import com.pstu.admissiontest.views.DancingScriptRegularFontTextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class WelcomeActivity extends AppCompatActivity {
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Handler mChangeActivityHandler = new Handler();
    private final Runnable mGoToMainActivityRunnable = new Runnable() {
        @Override
        public void run() {
            if(checkPermission()) {
                prepareData();
            }
            else requestPermission();
        }
    };

    private DBHandler dbHandler;
    private final int resource1 = R.raw.seat_plan_a_unit;
    private final int resource2 = R.raw.seat_plan_b_unit;
    private final int resource3 = R.raw.seat_plan_c_unit;
    private DancingScriptRegularFontTextView statusTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mContentView = findViewById(R.id.fullscreen_content);
        statusTxt = findViewById(R.id.status);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //hide the action bar and status bar - to make the activity fullscreen
        hide();

        if(getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }else {
            mChangeActivityHandler.removeCallbacks(mGoToMainActivityRunnable);
            mChangeActivityHandler.postDelayed(mGoToMainActivityRunnable, 1500);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mChangeActivityHandler.removeCallbacks(mGoToMainActivityRunnable);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
        mHideHandler.postDelayed(mHidePart2Runnable, 0);
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                FinalVariables.STORAGE_ACCESS_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case FinalVariables.STORAGE_ACCESS_PERMISSION_REQUEST_CODE:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean WriteStoragePermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && WriteStoragePermission) {
                        Toast.makeText(WelcomeActivity.this,"All set! Enjoy.",Toast.LENGTH_LONG).show();
                        prepareData();
                    } else {
                        Toast.makeText(WelcomeActivity.this,"Permission Denied!",Toast.LENGTH_LONG).show();
                        requestPermission();
                    }
                }
                break;
        }
    }

    private void prepareData(){
        dbHandler = new DBHandler(this);
        try{
            if(dbHandler.getRowCount(dbHandler.TABLE_A_UNIT) <= 0){
                statusTxt.setText("Initializing.\nThis may take a few moment. Please wait...");
                readAndSaveData(resource1, dbHandler.TABLE_A_UNIT);
            }
            if(dbHandler.getRowCount(dbHandler.TABLE_B_UNIT) <= 0){
                readAndSaveData(resource2, dbHandler.TABLE_B_UNIT);
            }
            if(dbHandler.getRowCount(dbHandler.TABLE_C_UNIT) <= 0){
                readAndSaveData(resource3, dbHandler.TABLE_C_UNIT);
            }
        }catch (IOException e){
            e.printStackTrace();
            statusTxt.setText("Something's wrong. \nPlease, uninstall the app and install again.");
        }finally {
            statusTxt.setText("Keep calm and have a good exam :)");
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void readAndSaveData(int resource, String tableName) throws IOException{
        Log.d("Creating Database for: "+tableName, "Starting process...");
        final Resources resources = getApplicationContext().getResources();
        InputStream inputStream = resources.openRawResource(resource);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String line = "";
            String hallNumber = "";
            String roomNumber = "";
            String center = "";
            String roomName = "";
            String strtRoll = "";
            String endRoll = "";
            String total = "";
            long inserted = 0;
            int sl = 0;
            while ((line = reader.readLine()) != null){
                if(line.contains(".0")) line = line.replace(".0", "").trim();
                if(sl==0){
                    hallNumber = line;
                    sl+=1;
                }else if(sl==1){
                    roomNumber = line;
                    sl+=1;
                }else if(sl==2){
                    center = line;
                    sl+=1;
                }else if(sl==3){
                    roomName = line;
                    sl+=1;
                }else if(sl==4){
                    sl+=1;
                }else if(sl==5){
                    strtRoll = line;
                    sl+=1;
                }else if(sl==6){
                    endRoll = line;
                    sl+=1;
                }else if(sl==7){
                    total = line;
                    inserted = dbHandler.insertItem(
                            new Item(hallNumber, roomNumber, center, roomName, strtRoll, endRoll, total), tableName);
                    if(inserted>0){
                        Log.d("Creating Database: ", "Inserted: "+strtRoll+","+endRoll+","+roomName);
                    }else {
                        Log.d("Creating Database: ", "Insertion failed: "+strtRoll+","+endRoll+","+roomName);
                    }
                    sl = 0;
                }else{
                    Toast.makeText(getApplicationContext(), "Initialization failed!", Toast.LENGTH_SHORT).show();
                }

            }
        }finally {
            reader.close();
        }
        Log.d("Creating Database for: "+tableName, "Done process");
    }
}