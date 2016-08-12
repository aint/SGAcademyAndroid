package com.github.aint.lesson7.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.aint.lesson7.R;
import com.github.aint.lesson7.adapter.MessageArrayAdapter;
import com.github.aint.lesson7.model.Message;
import com.github.aint.lesson7.service.MessageService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = MainActivity.class.getName();

    public static final String MESSAGE_ATTRIBUTE = "message";
    public static final String MESSAGES_ATTRIBUTE = "messages";
    public static final String NEW_MESSAGE_ACTION = "new_message";

    @BindView(R.id.listView) ListView listView;
    @BindView(R.id.empty) TextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        startMessageService();
        initListView();
        setNoMessageTextView();
    }

    private void initListView() {
        listView.setAdapter(new MessageArrayAdapter(this, App.getAllMessages()));
        listView.setOnItemClickListener(this);
    }

    private void startMessageService() {
        startService(new Intent(this, MessageService.class));
    }

    private void setNoMessageTextView() {
        emptyTextView.setText(getResources().getText(R.string.no_message_textview));
        listView.setEmptyView(emptyTextView);
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
