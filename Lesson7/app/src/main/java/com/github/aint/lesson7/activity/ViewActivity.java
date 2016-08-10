package com.github.aint.lesson7.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.aint.lesson7.R;
import com.github.aint.lesson7.model.Message;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewActivity extends Activity {

    private Message message;

    @BindView(R.id.firstNameTextView) TextView firstNameTextView;
    @BindView(R.id.lastNameTextView) TextView lastNameTextView;
    @BindView(R.id.ageTextView) TextView ageTextView;
    @BindView(R.id.sexTextView) TextView sexTextView;
    @BindView(R.id.salaryTextView) TextView salaryTextView;
    @BindView(R.id.locationTextView) TextView locationTextView;
    @BindView(R.id.occupationTextView) TextView occupationTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_and_view_layout);
        ButterKnife.bind(this);

        setVisibleViewComponents();
        setInvisibleAddComponents();

        message = getMessage();
        setTextViews();
    }

    private void setVisibleViewComponents() {
        findViewById(R.id.viewLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.backButton).setVisibility(View.VISIBLE);
        findViewById(R.id.exitButton).setVisibility(View.VISIBLE);
    }

    private void setInvisibleAddComponents() {
        findViewById(R.id.editLayout).setVisibility(View.GONE);
        findViewById(R.id.cancelButton).setVisibility(View.GONE);
        findViewById(R.id.saveButton).setVisibility(View.GONE);
    }

    private Message getMessage() {
        return (Message) getIntent().getSerializableExtra(MainActivity.PERSON_ATTRIBUTE);
    }

    private void setTextViews() {
//        firstNameTextView.setText(person.getFirstName());
//        lastNameTextView.setText(person.getLastName());
//        ageTextView.setText(String.valueOf(person.getAge()));
//        sexTextView.setText(person.getSex());
//        locationTextView.setText(person.getLocation());
//        salaryTextView.setText(String.valueOf(person.getSalary()));
//        occupationTextView.setText(person.getOccupation());
    }

    public void onBackButtonClick(View view) {
        onBackPressed();
    }

    public void onExitButtonClick(View view) {
        finish();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

}
