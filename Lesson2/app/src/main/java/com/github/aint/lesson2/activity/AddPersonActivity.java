package com.github.aint.lesson2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.github.aint.lesson2.R;
import com.github.aint.lesson2.model.Person;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.github.aint.lesson2.activity.MainActivity.PERSON_KEYS;
import static com.github.aint.lesson2.activity.MainActivity.PERSON_PREFS_NAME;

public class AddPersonActivity extends Activity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);

        sharedPreferences = getSharedPreferences(PERSON_PREFS_NAME, Context.MODE_APPEND);
    }

    public void onSaveButtonClick(View view) {
        Person person = new Person(
                ((EditText) findViewById(R.id.firstNameEditText)).getText().toString(),
                ((EditText) findViewById(R.id.lastNameEditText)).getText().toString(),
                Integer.valueOf(((EditText) findViewById(R.id.ageEditText)).getText().toString()),
                ((EditText) findViewById(R.id.sexEditText)).getText().toString(),
                Double.valueOf(((EditText) findViewById(R.id.salaryEditText)).getText().toString()),
                ((EditText) findViewById(R.id.locationEditText)).getText().toString(),
                ((EditText) findViewById(R.id.occupationEditText)).getText().toString()
        );

        new WritePersonsToPrefsTask().execute(person
//                new Person("Ivan", "Ivanov", 25, "male", 999.9, "Rivne", "Developer"),
//                new Person("Stepan", "Stepanov", 15, "male", 777.7, "Lviv", "Designer"),
//                new Person("Petro", "Petrov", 20, "male", 555.5, "Lursk", "Tester")
        );
        startActivity(new Intent(this, MainActivity.class));
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

    public void onCancelButtonClick(View view) {
        onBackPressed();
    }

}
