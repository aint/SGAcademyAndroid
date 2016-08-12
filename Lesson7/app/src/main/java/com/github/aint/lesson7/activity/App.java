package com.github.aint.lesson7.activity;

import android.app.Application;
import android.util.Log;

import com.github.aint.lesson7.database.asynctask.ReadMessagesFromDbTask;
import com.github.aint.lesson7.model.Message;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class App extends Application {

    private static final String LOG_TAG = App.class.getName();

    private static List<Message> messages;

    @Override
    public void onCreate() {
        super.onCreate();

        messages = readMessagesFromDb();
    }

    private List<Message> readMessagesFromDb() {
        try {
            return new ReadMessagesFromDbTask(this).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Log.e(LOG_TAG, sw.toString());
        }
        return new ArrayList<>();
    }

    public static List<Message> getAllMessages() {
        return messages;
    }

    public static void addMessage(Message message) {
        messages.add(message);
    }

}
