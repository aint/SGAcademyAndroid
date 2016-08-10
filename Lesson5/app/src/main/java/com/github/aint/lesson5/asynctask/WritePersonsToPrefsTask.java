package com.github.aint.lesson5.asynctask;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.github.aint.lesson5.PersonDbHelper;
import com.github.aint.lesson5.PersonEntity;
import com.github.aint.lesson5.model.Person;

public class WritePersonsToPrefsTask extends AsyncTask<Person, Void, Void> {

    private SQLiteDatabase sqLiteDb;

    public WritePersonsToPrefsTask(Context context) {
        this.sqLiteDb = new PersonDbHelper(context).getWritableDatabase();
    }

    @Override
    protected Void doInBackground(Person... persons) {
        insertToDb(persons);
        return null;
    }

    private void insertToDb(Person... persons) {
        for (Person person : persons) {
            sqLiteDb.insert(PersonEntity.TABLE_NAME, null, constructContentValues(person));
        }
    }

    private ContentValues constructContentValues(Person person) {
        ContentValues values = new ContentValues();
        values.put(PersonEntity.COLUMN_NAME_IMAGE_ID, person.getImageId());
        values.put(PersonEntity.COLUMN_NAME_FIRST_NAME, person.getFirstName());
        values.put(PersonEntity.COLUMN_NAME_LAST_NAME, person.getLastName());
        values.put(PersonEntity.COLUMN_NAME_AGE, person.getAge());
        values.put(PersonEntity.COLUMN_NAME_SEX, person.getSex());
        values.put(PersonEntity.COLUMN_NAME_SALARY, person.getSalary());
        values.put(PersonEntity.COLUMN_NAME_LOCATION, person.getLocation());
        values.put(PersonEntity.COLUMN_NAME_OCCUPATION, person.getOccupation());
        return values;
    }
}
