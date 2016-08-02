package com.github.aint.lesson2.activity;

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

import com.github.aint.lesson2.R;
import com.github.aint.lesson2.adapter.PersonArrayAdapter;
import com.github.aint.lesson2.model.Person;

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
    private List<Person> persons;
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
            persons = new ReadPersonsFromPrefsTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Log.e(LOG_TAG, sw.toString());
        }
    }

    private void setPersonsToAdapter() {
        listView.setAdapter(new PersonArrayAdapter(this, persons));
    }

    private class ReadPersonsFromPrefsTask extends AsyncTask<Void, Void, List<Person>> {

        @Override
        protected List<Person> doInBackground(Void... params) {
            return getPersonsFromPrefs();
        }

        private List<Person> getPersonsFromPrefs() {
            return stringSetToPersonList();
        }

        private List<Person> stringSetToPersonList() {
            List<Person> personList = new ArrayList<>();
            for (String value : sharedPreferences.getStringSet(PERSON_KEYS, new HashSet<String>())) {
                personList.add(stringToPerson(value));
            }
            return personList;
        }

        private Person stringToPerson(String str) {
            String[] fields = str.split(":");
            return new Person(
                    Integer.valueOf(fields[0]),
                    fields[1],
                    fields[2],
                    Integer.valueOf(fields[3]),
                    fields[4],
                    Double.valueOf(fields[5]),
                    fields[6],
                    fields[7]
            );
        }

    }

    private void setNoPersonTextView() {
        findViewById(R.id.noPersonTextView).setVisibility(persons.isEmpty() ? View.VISIBLE : View.GONE);
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
        startActivity(new Intent(this, AddPersonActivity.class));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this, ViewActivity.class)
                .putExtra(PERSON_ATTRIBUTE, (Person) parent.getItemAtPosition(position)));
    }

}
