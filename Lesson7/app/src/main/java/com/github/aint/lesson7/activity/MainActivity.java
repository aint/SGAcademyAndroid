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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.aint.lesson7.R;
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

    private List<String> messages;
    @BindView(R.id.listView) ListView listView;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("new message".equals(intent.getAction())) {
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                        new Intent(context, MainActivity.class)
                                .putStringArrayListExtra("messages", intent.getStringArrayListExtra("messages")),
                        FLAG_UPDATE_CURRENT);

                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                        .notify(101, new NotificationCompat.Builder(context)
                                .setAutoCancel(true)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Title")
                                .setContentText("Text")
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

        messages = createMsg();
        try {
            listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                    getIntent().getStringArrayListExtra("messages")));
        } catch (Exception e) {
            regReceiverAndStartService();
            listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages));
        }

        setNoPersonTextView();

    }

    private void regReceiverAndStartService() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("new message");

        registerReceiver(broadcastReceiver, intentFilter);

        Intent intent = new Intent(this, MessageService.class);
        intent.putStringArrayListExtra("messages", (ArrayList<String>) messages);
        startService(intent);
    }

    private List<String> createMsg() {
        messages = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            messages.add("old msg");
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
