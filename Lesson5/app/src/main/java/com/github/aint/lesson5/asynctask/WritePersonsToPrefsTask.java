package com.github.aint.lesson5.asynctask;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.github.aint.lesson5.model.Person;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.github.aint.lesson5.activity.MainActivity.PERSON_KEYS;

public class WritePersonsToPrefsTask extends AsyncTask<Person, Void, Void> {

    private final SharedPreferences sharedPreferences;

    public WritePersonsToPrefsTask(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

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
        Set<String> personSet = new LinkedHashSet<>();
        for (Person person : persons) {
            personSet.add(personToString(person));
        }
        return personSet;
    }

    private String personToString(Person person) {
        return new Gson().toJson(person);
    }
}
