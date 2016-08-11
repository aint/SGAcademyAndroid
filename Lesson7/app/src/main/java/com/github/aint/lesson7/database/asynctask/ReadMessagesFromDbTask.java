package com.github.aint.lesson7.database.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.github.aint.lesson7.database.entry.MessageEntry;
import com.github.aint.lesson7.database.helper.MessageDbHelper;
import com.github.aint.lesson7.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ReadMessagesFromDbTask extends AsyncTask<Void, Void, List<Message>> {

    private SQLiteDatabase sqLiteDb;

    public ReadMessagesFromDbTask(Context context) {
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
                cursor.getInt(cursor.getColumnIndexOrThrow(MessageEntry.COLUMN_NAME_IMAGE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(MessageEntry.COLUMN_NAME_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(MessageEntry.COLUMN_NAME_LAST_NAME))
        );
    }

    private Cursor selectAllCursor() {
        return sqLiteDb.query(MessageEntry.TABLE_NAME, null, null, null, null, null, null);
    }

}
