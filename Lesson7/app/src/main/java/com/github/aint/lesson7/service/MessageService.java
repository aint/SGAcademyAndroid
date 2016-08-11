package com.github.aint.lesson7.service;

import android.app.IntentService;
import android.content.Intent;

import com.github.aint.lesson7.model.Message;

import java.util.concurrent.TimeUnit;

import static com.github.aint.lesson7.activity.MainActivity.MESSAGES_ATTRIBUTE;
import static com.github.aint.lesson7.activity.MainActivity.NEW_MESSAGE_ACTION;

public class MessageService extends IntentService {

    public MessageService() {
        super(MessageService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        for (int i = 5; i < 100; i++) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendBroadcast(new Intent(NEW_MESSAGE_ACTION).putExtra(MESSAGES_ATTRIBUTE,
                    new Message(0, "Title " + i, "Body " + i)));
        }
    }

}
