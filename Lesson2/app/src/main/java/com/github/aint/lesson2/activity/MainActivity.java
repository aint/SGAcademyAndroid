package com.github.aint.lesson2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.github.aint.lesson2.R;
import com.github.aint.lesson2.model.Person;

public class MainActivity extends AppCompatActivity {

    public static final String PERSON_ATTRIBUTE = "person";
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        person = initPerson();
        setTextView();
    }

    private Person initPerson() {
        return new Person("Ivan", "Ivanov", 25, "male", 999.9, "Rivne", "Developer");
    }

    private void setTextView() {
        ((TextView) findViewById(R.id.fullNameTextView)).setText(person.getFirstName() + " " + person.getLastName());
    }

    public void onViewButtonClick(View view) {
        startActivity(new Intent(this, ViewActivity.class).putExtra(PERSON_ATTRIBUTE, person));
    }

    public void onExitButtonClick(View view) {
        finish();
    }

}
