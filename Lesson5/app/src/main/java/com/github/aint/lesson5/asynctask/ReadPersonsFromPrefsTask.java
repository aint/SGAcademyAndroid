package com.github.aint.lesson5.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.github.aint.lesson5.PersonDbHelper;
import com.github.aint.lesson5.PersonEntity;
import com.github.aint.lesson5.model.Person;

import java.util.ArrayList;
import java.util.List;

public class ReadPersonsFromPrefsTask extends AsyncTask<Void, Void, List<Person>> {

    private SQLiteDatabase sqLiteDb;

    public ReadPersonsFromPrefsTask(Context context) {
        this.sqLiteDb = new PersonDbHelper(context).getReadableDatabase();
    }

    @Override
    protected List<Person> doInBackground(Void... params) {
        return getPersonsFromPrefs();
    }

    private List<Person> getPersonsFromPrefs() {
        List<Person> personList = new ArrayList<>();
        Cursor cursor = selectAllCursor();
        while (cursor.moveToNext()) {
            personList.add(constructPerson(cursor));
        }
        return personList;
    }

    private Person constructPerson(Cursor cursor) {
        return new Person(
                cursor.getInt(cursor.getColumnIndexOrThrow(PersonEntity.COLUMN_NAME_IMAGE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(PersonEntity.COLUMN_NAME_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(PersonEntity.COLUMN_NAME_LAST_NAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(PersonEntity.COLUMN_NAME_AGE)),
                cursor.getString(cursor.getColumnIndexOrThrow(PersonEntity.COLUMN_NAME_SEX)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(PersonEntity.COLUMN_NAME_SALARY)),
                cursor.getString(cursor.getColumnIndexOrThrow(PersonEntity.COLUMN_NAME_LOCATION)),
                cursor.getString(cursor.getColumnIndexOrThrow(PersonEntity.COLUMN_NAME_OCCUPATION))
        );
    }

    private Cursor selectAllCursor() {
        return sqLiteDb.query(PersonEntity.TABLE_NAME, null, null, null, null, null, null);
    }

}
