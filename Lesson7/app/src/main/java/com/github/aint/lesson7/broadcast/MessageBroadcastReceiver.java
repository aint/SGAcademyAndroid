package com.github.aint.lesson7.broadcast;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.github.aint.lesson7.R;
import com.github.aint.lesson7.activity.MainActivity;
import com.github.aint.lesson7.model.Message;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.github.aint.lesson7.activity.MainActivity.MESSAGES_ATTRIBUTE;
import static com.github.aint.lesson7.activity.MainActivity.NEW_MESSAGE_ACTION;

public class MessageBroadcastReceiver extends BroadcastReceiver {

    private Context context;
    private Message message;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        message = (Message) intent.getSerializableExtra(MESSAGES_ATTRIBUTE);

        if (NEW_MESSAGE_ACTION.equals(intent.getAction())) {
            showNotification();
        }
    }

    private void showNotification() {
        ((NotificationManager) context.getSystemService(NOTIFICATION_SERVICE))
                .notify(101, new NotificationCompat.Builder(context)
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(message.getTitle())
                        .setContentText(message.getBody())
                        .setContentIntent(getPendingIntent())
                        .build()
                );
    }

    private PendingIntent getPendingIntent() {
        return PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class).putExtra(MESSAGES_ATTRIBUTE, message), FLAG_UPDATE_CURRENT);
    }

}
