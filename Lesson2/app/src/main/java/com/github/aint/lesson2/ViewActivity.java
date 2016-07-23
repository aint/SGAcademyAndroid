package com.github.aint.lesson2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class ViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_layout);
    }

    public void onBackButtonClick(View view) {
        onBackPressed();
    }

}
