package com.pstu.admissiontest.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.transition.Visibility;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.pstu.admissiontest.R;
import com.pstu.admissiontest.database.DBHandler;
import com.pstu.admissiontest.utils.Search;
import com.pstu.admissiontest.variables.FinalVariables;
import com.pstu.admissiontest.views.DancingScriptRegularFontTextView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage, mSelectedUnit, mSave, mLocation;
    private ImageView imgLocation;
    private FloatingSearchView mSearchView;

    private Search search;
    private String selectedUnit = FinalVariables.A_UNIT;
    private ArrayList<String> result;

    // # milliseconds, desired time passed between two back presses.
    private static final int TIME_INTERVAL = 2500;
    private long mBackPressed;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.unit_a:
                    selectedUnit = FinalVariables.A_UNIT;
                    mSearchView.clearFocus();
                    mSearchView.clearQuery();
                    mSearchView.setSearchHint("Search for A unit");
                    mTextMessage.setText("Selected unit A\n\nEnter a valid roll to search");
                    mSelectedUnit.setText("A\nUnit");
                    toggleVisibility(View.INVISIBLE);
                    return true;
                case R.id.unit_b:
                    selectedUnit = FinalVariables.B_UNIT;
                    mSearchView.clearFocus();
                    mSearchView.clearQuery();
                    mSearchView.setSearchHint("Search for B unit");
                    mTextMessage.setText("Selected unit B\n\nEnter a valid roll to search");
                    mSelectedUnit.setText("B\nUnit");
                    toggleVisibility(View.INVISIBLE);
                    return true;
                case R.id.unit_c:
                    selectedUnit = FinalVariables.C_UNIT;
                    mSearchView.clearFocus();
                    mSearchView.clearQuery();
                    mSearchView.setSearchHint("Search for C unit");
                    mTextMessage.setText("Selected unit C\n\nEnter a valid roll to search");
                    mSelectedUnit.setText("C\nUnit");
                    toggleVisibility(View.INVISIBLE);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mTextMessage = (TextView) findViewById(R.id.message);
        mSelectedUnit = (TextView) findViewById(R.id.selected_unit);
        mSave = (TextView) findViewById(R.id.save);
        mLocation = (TextView) findViewById(R.id.location);
        imgLocation = (ImageView) findViewById(R.id.ic_location);
        initSearchView();

        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLocation();
            }
        });

        imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLocation();
            }
        });

    }

    private void findLocation(){
        if(result != null && !result.isEmpty()) {
            String placeName = result.get(6);
            //String mode = "&mode=w"; //optional; d = drive, w = walk, b = bycycle
            try {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+placeName);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }catch (Exception e){
                Toast.makeText(MainActivity.this, "Sorry, Location is not supported in your device",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initSearchView(){
        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        mSearchView.setDimBackground(false);
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                doSearch(newQuery);
            }
        });


        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_about:
                        startActivity(new Intent(MainActivity.this, DeveloperActivity.class));
                        break;
                    case R.id.action_share:
                        shareApp();
                        break;
                    case R.id.action_help:
                        admissionHelpApp();
                        break;
                    default:
                        break;
                }
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
            }

            @Override
            public void onFocusCleared() {
                mSearchView.clearQuery();
            }
        });
    }

    private void doSearch(String newQuery){
        toggleVisibility(View.INVISIBLE);
        String str = "";
        int ln = 0, color = Color.BLACK;
        if(newQuery!=null && !newQuery.isEmpty()) ln = newQuery.length();

        if(ln<FinalVariables.defaultRollLength){
            str += "Enter a valid roll to search";

        } else if(ln>FinalVariables.MaxRollLength){
            color = Color.RED;
            str += "Invalid roll.\n" +
                    "Recommended: Enter the correct roll and search again.";

        }else {
            search = new Search(newQuery, selectedUnit, MainActivity.this);
            result = new ArrayList<>();
            result = search.searchInDatabase();

            if(result.isEmpty()) {
                color = Color.RED;
                str += "Search result in "+selectedUnit
                        +"\n\nNot found.\n\nMay be an invalid roll or wrong unit.\n" +
                        "Recommended: Enter the correct roll, select the correct unit and search again.";
            }else {
                toggleVisibility(View.VISIBLE);
                str += "Search result in "+selectedUnit
                        +"\n\nRoll: "+newQuery
                        +"\n\nCenter: "+result.get(3)
                        +"\n\nRoom number: "+result.get(2)
                        +"\n\nRoom name: "+result.get(4);
            }
        }

        mTextMessage.setTextColor(color);
        mTextMessage.setText(str);
    }

    private void toggleVisibility(int visibility){
        //mSave.setVisibility(visibility);
        mLocation.setVisibility(visibility);
        imgLocation.setVisibility(visibility);

        if(visibility==View.VISIBLE) {
            mSelectedUnit.setVisibility(View.INVISIBLE);

        }else {
            mSelectedUnit.setVisibility(View.VISIBLE);
        }
    }

    private void shareApp(){
        ApplicationInfo info = getApplicationContext().getApplicationInfo();
        String path = info.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
        startActivity(intent.createChooser(intent,"Share app via "));
    }

    /**
     * Admission help app link
     */
    private void admissionHelpApp(){
        String chooserTitle = "Options";
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.pstuhelpline.rickyislam.myapplication&hl=en");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        startActivity(Intent.createChooser(intent, chooserTitle));
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            //super.onBackPressed();
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
            return;
        } else {
            Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

}
