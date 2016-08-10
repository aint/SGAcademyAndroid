package com.github.aint.lesson7.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.github.aint.lesson5.PersonDbHelper;
import com.github.aint.lesson5.PersonEntity;
import com.github.aint.lesson5.model.Person;
import com.github.aint.lesson7.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ReadPersonsFromPrefsTask extends AsyncTask<Void, Void, List<Message>> {

    private SQLiteDatabase sqLiteDb;

    public ReadPersonsFromPrefsTask(Context context) {
        this.sqLiteDb = new MessageDbHelper(context).getReadableDatabase();
    }

    @Override
    protected List<Message> doInBackground(Void... params) {
        return getPersonsFromPrefs();
    }

    private List<Message> getPersonsFromPrefs() {
        List<Message> messageList = new ArrayList<>();
        Cursor cursor = selectAllCursor();
        while (cursor.moveToNext()) {
            messageList.add(constructPerson(cursor));
        }
        return messageList;
    }

    private Message constructPerson(Cursor cursor) {
        return new Message(
                cursor.getInt(cursor.getColumnIndexOrThrow(MessageEntity.COLUMN_NAME_IMAGE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(MessageEntity.COLUMN_NAME_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(MessageEntity.COLUMN_NAME_LAST_NAME)),
        );
    }

    private Cursor selectAllCursor() {
        return sqLiteDb.query(MessageEntity.TABLE_NAME, null, null, null, null, null, null);
    }

}
