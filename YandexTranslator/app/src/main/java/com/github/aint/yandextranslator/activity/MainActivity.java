package com.github.aint.yandextranslator.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.aint.yandextranslator.R;
import com.github.aint.yandextranslator.request.QueryImpl;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            String hello = new Translator().execute("cat is smart").get();
            Log.e(TAG, "onCreate: " + hello);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private class Translator extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return new QueryImpl().accept(params[0]);
        }
    }

}
