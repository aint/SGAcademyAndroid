package com.github.aint.lesson2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.aint.lesson2.R;
import com.github.aint.lesson2.model.Person;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.github.aint.lesson2.activity.MainActivity.PERSON_KEYS;
import static com.github.aint.lesson2.activity.MainActivity.PERSON_PREFS_NAME;

public class AddPersonActivity extends Activity {

    private SharedPreferences sharedPreferences;
    private String firstName;
    private String lastName;

    @BindView(R.id.firstNameEditText) EditText firstNameEditText;
    @BindView(R.id.lastNameEditText) EditText lastNameEditText;
    @BindView(R.id.ageEditText) EditText ageEditText;
    @BindView(R.id.sexEditText) EditText sexEditText;
    @BindView(R.id.salaryEditText) EditText salaryEditText;
    @BindView(R.id.locationEditText) EditText locationEditText;
    @BindView(R.id.occupationEditText) EditText occupationEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_and_view_layout);
        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences(PERSON_PREFS_NAME, Context.MODE_PRIVATE);

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
                    .clear()
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

        finishAndStartMainActivity();
    }

    private Person constructNewPerson() {
        return new Person(firstName, lastName,
                Integer.valueOf(getZeroIfEmpty(ageEditText.getText())),
                getDashIfEmpty(sexEditText.getText()),
                Double.valueOf(getZeroIfEmpty(salaryEditText.getText())),
                getDashIfEmpty(locationEditText.getText()),
                getDashIfEmpty(locationEditText.getText())
        );
    }

    private String getDashIfEmpty(Editable text) {
        return text.toString().isEmpty() ? "-" : text.toString();
    }

    private String getZeroIfEmpty(Editable text) {
        return text.toString().isEmpty() ? "0" : text.toString();
    }

    private boolean validateFullName() {
        firstName = firstNameEditText.getText().toString();
        lastName = lastNameEditText.getText().toString();
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

    private void finishAndStartMainActivity() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onCancelButtonClick(View view) {
        finishAndStartMainActivity();
    }

}
