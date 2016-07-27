package com.github.aint.lesson2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.github.aint.lesson2.R;
import com.github.aint.lesson2.model.Person;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends Activity implements View.OnClickListener {

    public static final String PERSON_ATTRIBUTE = "person";

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PERSON_KEYS = "person_keys";
    private static final int TEXT_SIZE = 30;

    private SharedPreferences sharedPreferences;
    private List<Person> mPersons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("person_prefs", Context.MODE_PRIVATE);

        setPersons();
        getPersons();

        displayPersons();

        setNoPersonTextView();
    }

    private void getPersons() {
        try {
            mPersons = new ReadPersonsFromPrefsTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Log.e(LOG_TAG, sw.toString());
        }
    }

    private class ReadPersonsFromPrefsTask extends AsyncTask<Void, Void, List<Person>> {

        @Override
        protected List<Person> doInBackground(Void... params) {
            return getPersonsFromPrefs();
        }

        private List<Person> getPersonsFromPrefs() {
            return stringSetToPersonList(sharedPreferences.getStringSet(PERSON_KEYS, null));
        }

        private List<Person> stringSetToPersonList(Set<String> personSet) {
            List<Person> personList = new ArrayList<>();
            if (personSet != null) {
                for (String value : personSet) {
                    personList.add(stringToPerson(value));
                }
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

    private void setPersons() {
        new WritePersonsToPrefsTask().execute(
                new Person("Ivan", "Ivanov", 25, "male", 999.9, "Rivne", "Developer"),
                new Person("Stepan", "Stepanov", 15, "male", 777.7, "Lviv", "Designer"),
                new Person("Petro", "Petrov", 20, "male", 555.5, "Lursk", "Tester")
        );
    }

    private class WritePersonsToPrefsTask extends AsyncTask<Person, Void, Void> {

        @Override
        protected Void doInBackground(Person... persons) {
            saveToPrefs(PERSON_KEYS, convertPersons(persons));
            return null;
        }

        private void saveToPrefs(String key, Set<String> values) {
            sharedPreferences.edit()
                    .putStringSet(key, values)
                    .apply();
        }

        private Set<String> convertPersons(Person... persons) {
            Set<String> personSet = new HashSet<>();
            for (Person person : persons) {
                personSet.add(personToString(person));
            }
            return personSet;
        }

        private String personToString(Person person) {
            return TextUtils.join(":", Arrays.asList(
                    person.getId(),
                    person.getFirstName(),
                    person.getLastName(),
                    String.valueOf(person.getAge()),
                    person.getSex(),
                    String.valueOf(person.getSalary()),
                    person.getLocation(),
                    person.getOccupation()
            ));
        }
    }

    private void displayPersons() {
        for (Person person : mPersons) {
            addPersonNameToLayout(person.getId(), person.getFirstName() + " " + person.getLastName());
        }
    }

    private void addPersonNameToLayout(int id, String fullName) {
        ((LinearLayout) findViewById(R.id.displayPersonLayout))
                .addView(createLinearLayoutWithText(id, createTextView(fullName)));

    }

    private TextView createTextView(String fullName) {
        TextView textView = new TextView(this);
        textView.setText(fullName);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private LinearLayout createLinearLayoutWithText(int id, TextView textView) {
        LinearLayout layout = new LinearLayout(this);
        layout.setId(id);
        layout.setLayoutParams(getCentredLayoutParams());
        layout.setOnClickListener(this);
        layout.addView(textView);
        return layout;
    }

    private LayoutParams getCentredLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        return layoutParams;
    }

    private void setNoPersonTextView() {
        findViewById(R.id.noPersonTextView).setVisibility(mPersons.isEmpty() ? View.VISIBLE : View.GONE);
    }

    public void onExitButtonClick(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        for (Person person : mPersons) {
            if (v.getId() == person.getId()) {
                startActivity(new Intent(this, ViewActivity.class).putExtra(PERSON_ATTRIBUTE, person));
                return;
            }
        }
    }

}
