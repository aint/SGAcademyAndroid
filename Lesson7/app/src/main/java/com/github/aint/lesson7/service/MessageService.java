package com.github.aint.lesson7.service;

import android.app.IntentService;
import android.content.Intent;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MessageService extends IntentService {

    public MessageService() {
        super("MessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<String> messages = intent.getStringArrayListExtra("messages");
        for (int i = 0; i < 100; i++) {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            messages.add("msg " + i);
            sendBroadcast(new Intent("new message").putStringArrayListExtra("messages", messages));
        }
    }
}
