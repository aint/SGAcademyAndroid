package com.github.aint.lesson7.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import com.github.aint.lesson7.activity.App;
import com.github.aint.lesson7.broadcast.MessageBroadcastReceiver;
import com.github.aint.lesson7.model.Message;

import java.util.concurrent.TimeUnit;

import static com.github.aint.lesson7.activity.MainActivity.MESSAGES_ATTRIBUTE;
import static com.github.aint.lesson7.activity.MainActivity.NEW_MESSAGE_ACTION;

public class MessageService extends IntentService {

    private BroadcastReceiver broadcastReceiver = new MessageBroadcastReceiver();

    public MessageService() {
        super(MessageService.class.getName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(broadcastReceiver, new IntentFilter(NEW_MESSAGE_ACTION));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int size = App.getAllMessages().size();
        for (int i = size; i < size + 100; i++) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendBroadcast(new Intent(NEW_MESSAGE_ACTION).putExtra(MESSAGES_ATTRIBUTE,
                    new Message(0, "Title " + i, "Body " + i)));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
