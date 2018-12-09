package com.pstu.admissiontest.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.pstu.admissiontest.R;

public class HelpActivity extends AppCompatActivity {

    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        overridePendingTransition(R.anim.right_to_left, R.anim.stay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.title_help);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.stay, R.anim.left_to_right);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
