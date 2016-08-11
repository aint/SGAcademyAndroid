package com.github.aint.lesson7.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.aint.lesson7.R;
import com.github.aint.lesson7.adapter.MessageArrayAdapter;
import com.github.aint.lesson7.database.asynctask.ReadMessagesFromDbTask;
import com.github.aint.lesson7.model.Message;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final String LOG_TAG = MainActivity.class.getName();

    public static final String PERSON_ATTRIBUTE = "person";

    private List<Message> messages;
    @BindView(R.id.listView) ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getMessages();
        initListView();
        setNoPersonTextView();
    }

    private void getMessages() {
        try {
            messages = new ReadMessagesFromDbTask(this).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Log.e(LOG_TAG, sw.toString());
        }
    }

    private void initListView() {
        listView.setOnItemClickListener(this);
        listView.setAdapter(new MessageArrayAdapter(this, messages));
    }

    private void setNoPersonTextView() {
        findViewById(R.id.noPersonTextView).setVisibility(messages.isEmpty() ? View.VISIBLE : View.GONE);
    }

    public void onExitButtonClick(View view) {
        finish();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void onAddButtonClick(View view) {
        finish();
//        startActivity(new Intent(this, AddPersonActivity.class));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this, ViewActivity.class)
                .putExtra(PERSON_ATTRIBUTE, (Message) parent.getItemAtPosition(position)));
    }

}
