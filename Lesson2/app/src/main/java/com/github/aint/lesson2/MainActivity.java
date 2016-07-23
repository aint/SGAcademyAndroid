package com.github.aint.lesson2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onExitButtonClick(View view) {
        finish();
    }

    public void onViewButtonClick(View view) {
        startActivity(new Intent(this, ViewActivity.class));
    }

}
