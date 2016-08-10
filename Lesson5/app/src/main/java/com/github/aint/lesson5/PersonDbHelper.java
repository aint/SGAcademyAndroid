package com.github.aint.lesson5;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PersonDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Person.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_PERSON =
            "CREATE TABLE " + PersonEntity.TABLE_NAME + " (" +
                    PersonEntity._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                    PersonEntity.COLUMN_NAME_IMAGE_ID + INTEGER_TYPE + COMMA_SEP +
                    PersonEntity.COLUMN_NAME_FIRST_NAME + TEXT_TYPE + COMMA_SEP +
                    PersonEntity.COLUMN_NAME_LAST_NAME + TEXT_TYPE + COMMA_SEP +
                    PersonEntity.COLUMN_NAME_AGE + INTEGER_TYPE + COMMA_SEP +
                    PersonEntity.COLUMN_NAME_SEX + TEXT_TYPE + COMMA_SEP +
                    PersonEntity.COLUMN_NAME_SALARY + REAL_TYPE + COMMA_SEP +
                    PersonEntity.COLUMN_NAME_LOCATION + TEXT_TYPE + COMMA_SEP +
                    PersonEntity.COLUMN_NAME_OCCUPATION + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PersonEntity.TABLE_NAME;

    public PersonDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PERSON);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
