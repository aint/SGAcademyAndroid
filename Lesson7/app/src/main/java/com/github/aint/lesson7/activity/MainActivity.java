package com.github.aint.lesson7.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.aint.lesson7.R;
import com.github.aint.lesson7.adapter.MessageArrayAdapter;
import com.github.aint.lesson7.database.asynctask.ReadMessagesFromDbTask;
import com.github.aint.lesson7.database.asynctask.WriteMessagesToDbTask;
import com.github.aint.lesson7.model.Message;
import com.github.aint.lesson7.service.MessageService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final String LOG_TAG = MainActivity.class.getName();

    public static final String MESSAGE_ATTRIBUTE = "message";
    public static final String MESSAGES_ATTRIBUTE = "messages";
    public static final String NEW_MESSAGE_ACTION = "new_message";

    @BindView(R.id.listView) ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        checkNewMessage();
        setNoMessageTextView();
        startMessageService();
        initListView();
    }

    private void initListView() {
        listView.setAdapter(new MessageArrayAdapter(this, getAllMessages()));
        listView.setOnItemClickListener(this);
    }

    private void startMessageService() {
        startService(new Intent(this, MessageService.class));
    }

    private void checkNewMessage() {
        Message msg = (Message) getIntent().getSerializableExtra(MESSAGES_ATTRIBUTE);
        if (msg != null) {
            new WriteMessagesToDbTask(this).execute(msg);
        }
    }

    private List<Message> getAllMessages() {
        try {
            return new ReadMessagesFromDbTask(this).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Log.e(LOG_TAG, sw.toString());
        }
        return new ArrayList<>();
    }

    private void setNoMessageTextView() {
        TextView noMessage = (TextView) findViewById(R.id.empty);
        noMessage.setText(getResources().getText(R.string.no_message_textview));
        listView.setEmptyView(noMessage);
    }

    public void onExitButtonClick(View view) {
        finish();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    //TODO move to MessageArrayAdapter
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this, ViewActivity.class)
                .putExtra(MESSAGE_ATTRIBUTE, (Message) parent.getItemAtPosition(position)));
    }

}
