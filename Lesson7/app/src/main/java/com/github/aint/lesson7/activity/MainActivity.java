package com.github.aint.lesson7.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.aint.lesson7.R;
import com.github.aint.lesson7.adapter.MessageArrayAdapter;
import com.github.aint.lesson7.model.Message;
import com.github.aint.lesson7.service.MessageService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final String LOG_TAG = MainActivity.class.getName();

    public static final String MESSAGE_ATTRIBUTE = "message";
    public static final String MESSAGES_ATTRIBUTE = "messages";
    public static final String NEW_MESSAGE_ACTION = "new_message";

    private List<Message> messages;
    @BindView(R.id.listView) ListView listView;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (NEW_MESSAGE_ACTION.equals(intent.getAction())) {
                Message newMessage = (Message) intent.getSerializableExtra(MESSAGES_ATTRIBUTE);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                        new Intent(context, MainActivity.class).putExtra(MESSAGES_ATTRIBUTE, newMessage), FLAG_UPDATE_CURRENT);

                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                        .notify(101, new NotificationCompat.Builder(context)
                                .setAutoCancel(true)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(newMessage.getTitle())
                                .setContentText(newMessage.getBody())
                                .setContentIntent(pendingIntent)
                                .build()
                        );
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        listView.setOnItemClickListener(this);

        regReceiverAndStartService();

        messages = createMsg();
        Message newMsg = (Message) getIntent().getSerializableExtra(MESSAGES_ATTRIBUTE);
        if (newMsg != null) {
            messages.add(newMsg);
        }
        listView.setAdapter(new MessageArrayAdapter(this, messages));

        setNoPersonTextView();

    }

    private void regReceiverAndStartService() {
        registerReceiver(broadcastReceiver, new IntentFilter(NEW_MESSAGE_ACTION));

        startService(new Intent(this, MessageService.class));
    }

    private List<Message> createMsg() {
        messages = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            messages.add(new Message(0, "Old title " + i, "Old body " + i));
        }
        return messages;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

//    private void getMessages() {
//        try {
//            messages = new ReadMessagesFromDbTask(this).execute().get();
//        } catch (InterruptedException | ExecutionException e) {
//            StringWriter sw = new StringWriter();
//            e.printStackTrace(new PrintWriter(sw));
//            Log.e(LOG_TAG, sw.toString());
//        }
//    }

    private void setNoPersonTextView() {
        findViewById(R.id.noPersonTextView).setVisibility(messages.isEmpty() ? View.VISIBLE : View.GONE);
    }

    public void onExitButtonClick(View view) {
        finish();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void onAddButtonClick(View view) {
        finish();
//        startActivity(new Intent(this, AddPersonActivity.class));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this, ViewActivity.class)
                .putExtra(MESSAGE_ATTRIBUTE, (Message) parent.getItemAtPosition(position)));
    }

}
