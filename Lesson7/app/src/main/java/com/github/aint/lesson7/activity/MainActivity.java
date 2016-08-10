package com.github.aint.lesson7.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.aint.lesson7.R;
import com.github.aint.lesson7.adapter.MessageArrayAdapter;
import com.github.aint.lesson7.model.Message;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final String LOG_TAG = MainActivity.class.getName();

    public static final String PERSON_PREFS_NAME = "person_prefs";
    public static final String PERSON_KEYS = "person_keys";
    public static final String PERSON_ATTRIBUTE = "person";

    private SharedPreferences sharedPreferences;
    private List<Message> messages;
    @BindView(R.id.listView) ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences(PERSON_PREFS_NAME, Context.MODE_PRIVATE);
        listView.setOnItemClickListener(this);

        getPersons();
        setPersonsToAdapter();
        setNoPersonTextView();
    }

    private void getPersons() {
        try {
            messages = new ReadPersonsFromPrefsTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Log.e(LOG_TAG, sw.toString());
        }
    }

    private void setPersonsToAdapter() {
        listView.setAdapter(new MessageArrayAdapter(this, messages));
    }

    private class ReadPersonsFromPrefsTask extends AsyncTask<Void, Void, List<Message>> {

        @Override
        protected List<Message> doInBackground(Void... params) {
            return getPersonsFromPrefs();
        }

        private List<Message> getPersonsFromPrefs() {
            return stringSetToPersonList();
        }

        private List<Message> stringSetToPersonList() {
            List<Message> messageList = new ArrayList<>();
            for (String value : sharedPreferences.getStringSet(PERSON_KEYS, new HashSet<String>())) {
                messageList.add(stringToPerson(value));
            }
            return messageList;
        }

        private Message stringToPerson(String str) {
            return null;
//            String[] fields = str.split(":");
//            return new Person(
//                    Integer.valueOf(fields[0]),
//                    fields[1],
//                    fields[2],
//                    Integer.valueOf(fields[3]),
//                    fields[4],
//                    Double.valueOf(fields[5]),
//                    fields[6],
//                    fields[7]
//            );
        }

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
