package com.github.aint.yandextranslator.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.github.aint.yandextranslator.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void translateText() {
        String textFrom = ((EditText) findViewById(R.id.textFrom)).getText().toString();
        hideKeyboard();
        String textTo = "";//new Translator().execute(textFrom).get();
        Log.e(TAG, textFrom +  " => " + textTo);
        ((EditText) findViewById(R.id.textTo)).setHint(textTo);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (R.id.menu_ok == itemId) {
            translateText();
        } else if (R.id.menu_lang == itemId) {

        }
        return super.onOptionsItemSelected(item);
    }

}
