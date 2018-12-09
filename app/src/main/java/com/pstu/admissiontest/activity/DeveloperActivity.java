package com.pstu.admissiontest.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.pstu.admissiontest.database.DBHandler;
import com.pstu.admissiontest.model.Item;
import com.pstu.admissiontest.R;
import com.pstu.admissiontest.variables.FinalVariables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DeveloperActivity extends AppCompatActivity implements View.OnTouchListener{
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


    private ImageView dev1Call, dev2Call, dev1Img, dev2Img, dev1Fb, dev2Fb, dev1Gmail, dev2Gmail;
    private Bitmap bitmap;

    private static final String EXTRA_TEXT = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        mContentView = findViewById(R.id.fullscreen_content);

        dev1Call = (ImageView) findViewById(R.id.dev1_call);
        dev2Call = (ImageView) findViewById(R.id.dev2_call);
        dev1Img = (ImageView) findViewById(R.id.dev1_img);
        dev2Img = (ImageView) findViewById(R.id.dev2_img);
        dev1Fb = (ImageView) findViewById(R.id.dev1_fb);
        dev2Fb = (ImageView) findViewById(R.id.dev2_fb);
        dev1Gmail = (ImageView) findViewById(R.id.dev1_gmail);
        dev2Gmail = (ImageView) findViewById(R.id.dev2_gmail);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arhan);
        dev1Img.setImageDrawable(makeRoundedBitmapImage(bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rujel);
        dev2Img.setImageDrawable(makeRoundedBitmapImage(bitmap));

        dev1Call.setOnTouchListener(this);
        dev2Call.setOnTouchListener(this);
        dev1Fb.setOnTouchListener(this);
        dev2Fb.setOnTouchListener(this);
        dev1Gmail.setOnTouchListener(this);
        dev2Gmail.setOnTouchListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //hide the action bar and status bar - to make the activity fullscreen
        hide();
    }

    @Override
    protected void onPause() {
        super.onPause();
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
            view.setAlpha(0.5f);
            return true;
        }
        if(motionEvent.getAction() != MotionEvent.ACTION_UP) return false;

        view.setAlpha(1.0f);

        if(view==dev1Call) call("+8801764515461");
        else if(view==dev2Call) call("+8801758951289");
        else if(view==dev1Fb) linkToFacebook("arhan.ashik");
        else if(view==dev2Fb) linkToFacebook("rujel.cse");
        else if(view==dev1Gmail) sendEmail("ashik.pstu.cse@gmail.com");
        else if(view==dev2Gmail) sendEmail("rujel.pstu@gmail.com");

        view.setPressed( !view.isPressed());

        return true;
    }

    private RoundedBitmapDrawable makeRoundedBitmapImage(Bitmap bmp){
        RoundedBitmapDrawable rbd = RoundedBitmapDrawableFactory.create(getResources(), bmp);
        rbd.setCircular(true);

        return rbd;
    }

    /**
     * call
     */
    private void call(String number){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
        else startActivity(callIntent);
    }

    /**
     * Facebook id link
     */
    private void linkToFacebook(String userName){

        String chooserTitle = "Options";
        Uri uri = Uri.parse("http://www.facebook.com/"+userName);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        startActivity(Intent.createChooser(intent, chooserTitle));
    }

    /**
     * Sending Email
     */
    private void sendEmail(String userEmail){
        String sub = "Admission test 2017";
        String body = "";
        String chooserTitle = "Options";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + userEmail));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(emailIntent, chooserTitle));
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new
                        String[]{CALL_PHONE},123);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults.length> 0) {
                    boolean CallPermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (CallPermission) {
                        Toast.makeText(this,"All set! Call now.",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Permission Denied!", Toast.LENGTH_LONG).show();
                        requestPermission();
                    }
                }
            break;
        }

    }
}