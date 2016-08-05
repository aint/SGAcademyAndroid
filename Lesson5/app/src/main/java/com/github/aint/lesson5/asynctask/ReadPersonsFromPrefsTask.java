package com.github.aint.lesson5.asynctask;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.github.aint.lesson5.model.Person;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.github.aint.lesson5.activity.MainActivity.PERSON_KEYS;

public class ReadPersonsFromPrefsTask extends AsyncTask<Void, Void, List<Person>> {

    private final SharedPreferences sharedPreferences;

    public ReadPersonsFromPrefsTask(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

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
