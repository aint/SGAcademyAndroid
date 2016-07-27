package com.github.aint.lesson2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.github.aint.lesson2.R;
import com.github.aint.lesson2.model.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends Activity implements View.OnClickListener {

    public static final String PERSON_ATTRIBUTE = "person";
    public static final String PERSON_KEYS = "person_keys";

    private SharedPreferences sharedPreferences;
    private List<Person> mPersons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("person_prefs", Context.MODE_PRIVATE);

        initPersons();

        mPersons = getPersonsFromPrefs();
        for (int i = 0; i < mPersons.size(); i++) {
            addPersonNameToLayout(100 + i, mPersons.get(i).getFirstName() + " " + mPersons.get(i).getLastName());
        }

        setNoPersonTextView();
    }

    private List<Person> getPersonsFromPrefs() {
        Set<String> personSet = sharedPreferences.getStringSet(PERSON_KEYS, null);
        List<Person> personList = new ArrayList<>();
        if (personSet != null) {
            for (String value : personSet) {
                personList.add(stringToPerson(value));
            }
        }
        return personList;
    }

    private void initPersons() {
        saveToPrefs(PERSON_KEYS, new HashSet<>(Arrays.asList(
                personToString(new Person("Ivan", "Ivanov", 25, "male", 999.9, "Rivne", "Developer")),
                personToString(new Person("Stepan", "Stepanov", 15, "male", 777.7, "Lviv", "Designer")),
                personToString(new Person("Petro", "Petrov", 20, "male", 555.5, "Lursk", "Tester"))
        )));
    }

    private void saveToPrefs(String key, Set<String> values) {
        sharedPreferences.edit()
                .putStringSet(key, values)
                .apply();
    }

    private Person stringToPerson(String str) {
        String[] fields = str.split(":");
        return new Person(fields[0], fields[1], Integer.valueOf(fields[2]), fields[3], Double.valueOf(fields[4]), fields[5], fields[6]);
    }

    private String personToString(Person person) {
        return TextUtils.join(":", Arrays.asList(
                person.getFirstName(),
                person.getLastName(),
                String.valueOf(person.getAge()),
                person.getSex(),
                String.valueOf(person.getSalary()),
                person.getLocation(),
                person.getOccupation()
        ));
    }

    private void addPersonNameToLayout(int id, String fullName) {
        TextView text = new TextView(this);
        text.setText(fullName);
        text.setTextSize(30);
        LinearLayout layout = new LinearLayout(this);
        layout.setId(id);
        LayoutParams layoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layout.setLayoutParams(layoutParams);
        layout.setOnClickListener(this);
        layout.addView(text);

        ((LinearLayout) findViewById(R.id.displayPersonLayout)).addView(layout);

    }

    @Override
    public void onClick(View v) {
        Person person = null;
        for (int i = 0; i < mPersons.size(); i++) {
            if (v.getId() == 100 + i) {
                person = mPersons.get(i);
            }
        }
        startActivity(new Intent(this, ViewActivity.class).putExtra(PERSON_ATTRIBUTE, person));
    }

    private void setNoPersonTextView() {
        findViewById(R.id.noPersonTextView).setVisibility(mPersons.isEmpty() ? View.VISIBLE : View.GONE);
    }

    public void onExitButtonClick(View view) {
        finish();
    }
}
