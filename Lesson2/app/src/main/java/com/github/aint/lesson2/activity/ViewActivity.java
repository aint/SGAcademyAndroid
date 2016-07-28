package com.github.aint.lesson2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.aint.lesson2.R;
import com.github.aint.lesson2.model.Person;

public class ViewActivity extends Activity {

    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_and_view_layout);

        setVisibleViewComponents();
        setInvisibleAddComponents();

        person = getPerson();
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

    private void setTextViews() {
        ((TextView) findViewById(R.id.firstNameTextView)).setText(person.getFirstName());
        ((TextView) findViewById(R.id.lastNameTextView)).setText(person.getLastName());
        ((TextView) findViewById(R.id.ageTextView)).setText(String.valueOf(person.getAge()));
        ((TextView) findViewById(R.id.sexTextView)).setText(person.getSex());
        ((TextView) findViewById(R.id.locationTextView)).setText(person.getLocation());
        ((TextView) findViewById(R.id.salaryTextView)).setText(String.valueOf(person.getSalary()));
        ((TextView) findViewById(R.id.occupationTextView)).setText(person.getOccupation());
    }

    private Person getPerson() {
        return (Person) getIntent().getSerializableExtra(MainActivity.PERSON_ATTRIBUTE);
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
