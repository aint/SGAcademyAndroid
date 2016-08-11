package com.github.aint.lesson7.database.asynctask;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.github.aint.lesson7.database.entry.MessageEntry;
import com.github.aint.lesson7.database.helper.MessageDbHelper;
import com.github.aint.lesson7.model.Message;

public class WriteMessagesToDbTask extends AsyncTask<Message, Void, Void> {

    private SQLiteDatabase sqLiteDb;

    public WriteMessagesToDbTask(Context context) {
        this.sqLiteDb = new MessageDbHelper(context).getWritableDatabase();
    }

    @Override
    protected Void doInBackground(Message... messages) {
        insertToDb(messages);
        return null;
    }

    private void insertToDb(Message... messages) {
        for (Message message : messages) {
            sqLiteDb.insert(MessageEntry.TABLE_NAME, null, constructContentValues(message));
        }
    }

    private ContentValues constructContentValues(Message message) {
        ContentValues values = new ContentValues();
        values.put(MessageEntry.COLUMN_NAME_IMAGE_ID, message.getImageId());
        values.put(MessageEntry.COLUMN_NAME_FIRST_NAME, message.getTitle());
        values.put(MessageEntry.COLUMN_NAME_LAST_NAME, message.getBody());
        return values;
    }
}
