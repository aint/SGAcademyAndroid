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
import android.widget.Toast;

import com.github.aint.lesson2.R;
import com.github.aint.lesson2.model.Person;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.github.aint.lesson2.activity.MainActivity.PERSON_KEYS;
import static com.github.aint.lesson2.activity.MainActivity.PERSON_PREFS_NAME;

public class AddPersonActivity extends Activity {

    private SharedPreferences sharedPreferences;
    private String firstName;
    private String lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_and_view_layout);

        sharedPreferences = getSharedPreferences(PERSON_PREFS_NAME, Context.MODE_APPEND);

        setVisibleAddComponents();
        setInvisibleViewComponents();
    }

    private class WritePersonsToPrefsTask extends AsyncTask<Person, Void, Void> {

        @Override
        protected Void doInBackground(Person... persons) {
            appendToPrefs(convertPersons(persons));
            return null;
        }

        private void appendToPrefs(Set<String> values) {
            Set<String> personSet = sharedPreferences.getStringSet(PERSON_KEYS, new HashSet<String>());
            personSet.addAll(values);
            sharedPreferences.edit()
                    .putStringSet(PERSON_KEYS, personSet)
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

    public void onSaveButtonClick(View view) {
        if (!validateFullName()) {
            return;
        }
        new WritePersonsToPrefsTask().execute(constructNewPerson());

        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    private Person constructNewPerson() {
        String age = ((EditText) findViewById(R.id.ageEditText)).getText().toString();
        String sex = ((EditText) findViewById(R.id.sexEditText)).getText().toString();
        String salary = ((EditText) findViewById(R.id.salaryEditText)).getText().toString();
        String location = ((EditText) findViewById(R.id.locationEditText)).getText().toString();
        String occupation = ((EditText) findViewById(R.id.occupationEditText)).getText().toString();
        return new Person(firstName, lastName,
                age.isEmpty() ? 0 : Integer.valueOf(age),
                sex.isEmpty() ? "_" : sex,
                salary.isEmpty() ? 0.0 : Double.valueOf(salary),
                location .isEmpty() ? "_" : location,
                occupation.isEmpty() ? "_" : occupation
        );
    }

    private boolean validateFullName() {
        firstName = ((EditText) findViewById(R.id.firstNameEditText)).getText().toString();
        lastName = ((EditText) findViewById(R.id.lastNameEditText)).getText().toString();
        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "Full name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setVisibleAddComponents() {
        findViewById(R.id.editLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.cancelButton).setVisibility(View.VISIBLE);
        findViewById(R.id.saveButton).setVisibility(View.VISIBLE);
    }

    private void setInvisibleViewComponents() {
        findViewById(R.id.viewLayout).setVisibility(View.GONE);
        findViewById(R.id.backButton).setVisibility(View.GONE);
        findViewById(R.id.exitButton).setVisibility(View.GONE);
    }

    public void onCancelButtonClick(View view) {
        onBackPressed();
    }

}
