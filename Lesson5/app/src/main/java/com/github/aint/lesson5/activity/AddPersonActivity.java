package com.github.aint.lesson5.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.github.aint.lesson5.R;
import com.github.aint.lesson5.asynctask.WritePersonsToPrefsTask;
import com.github.aint.lesson5.model.Person;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.github.aint.lesson5.activity.MainActivity.PERSON_PREFS_NAME;

public class AddPersonActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 101;
    private static final String NOTIFICATION_TICKER = "Person Added";
    private static final String NOTIFICATION_MESSAGE = "Tap to see details";
    private static final String VALIDATION_TOAST = "Full name is required";

    private String firstName;
    private String lastName;

    @BindView(R.id.firstNameEditText) EditText firstNameEditText;
    @BindView(R.id.lastNameEditText) EditText lastNameEditText;
    @BindView(R.id.ageEditText) EditText ageEditText;
    @BindView(R.id.sexEditText) EditText sexEditText;
    @BindView(R.id.salaryEditText) EditText salaryEditText;
    @BindView(R.id.locationEditText) EditText locationEditText;
    @BindView(R.id.occupationEditText) EditText occupationEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_person_layout);
        ButterKnife.bind(this);

//        addDefault4Persons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_person_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (R.id.menu_save == itemId) {
            onSaveMenuAction();
        } else if (R.id.menu_cancel == itemId) {
            finishAndStartMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSaveMenuAction() {
        if (validateFullName()) {
            new WritePersonsToPrefsTask(getSharedPreferences(PERSON_PREFS_NAME, MODE_PRIVATE))
                    .execute(constructNewPerson());
            showNotification();
            clearAllFields();
        }
    }

    private void showNotification() {
        //TODO pass flag to show new added person
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID,
                new NotificationCompat.Builder(this)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setContentTitle(firstName + " " + lastName)
                        .setContentText(NOTIFICATION_MESSAGE)
                        .setTicker(NOTIFICATION_TICKER)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .build()
        );
    }

    private void addDefault4Persons() {
        Person messi = new Person(R.drawable.messi, "Lionel", "Messi", 29, "Male", 12345, "Barcelona", "Forward");
        Person ronaldo = new Person(R.drawable.ronaldo, "Cristiano", "Ronaldo", 31, "Male", 23451, "Real madrid", "Forward");
        Person suarez = new Person(R.drawable.suarez, "Luis", "Suarez", 29, "Male", 34512, "Barcelona", "Forward");
        Person neuer = new Person(R.drawable.neuer, "Manuel", "Neuer", 30, "Male", 45123, "Bayern", "Goalkeeper");
        new WritePersonsToPrefsTask(getSharedPreferences(PERSON_PREFS_NAME, MODE_PRIVATE))
                .execute(messi, ronaldo, suarez, neuer);
    }

    private Person constructNewPerson() {
        return new Person(R.drawable.default_profile_image, firstName, lastName,
                Integer.valueOf(getZeroIfEmpty(ageEditText.getText())),
                getDashIfEmpty(sexEditText.getText()),
                Double.valueOf(getZeroIfEmpty(salaryEditText.getText())),
                getDashIfEmpty(locationEditText.getText()),
                getDashIfEmpty(locationEditText.getText())
        );
    }

    private String getDashIfEmpty(Editable text) {
        return text.toString().isEmpty() ? "-" : text.toString();
    }

    private String getZeroIfEmpty(Editable text) {
        return text.toString().isEmpty() ? "0" : text.toString();
    }

    private boolean validateFullName() {
        firstName = firstNameEditText.getText().toString();
        lastName = lastNameEditText.getText().toString();
        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, VALIDATION_TOAST, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void finishAndStartMainActivity() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void clearAllFields() {
        firstNameEditText.setText(null);
        lastNameEditText.setText(null);
        ageEditText.setText(null);
        sexEditText.setText(null);
        salaryEditText.setText(null);
        locationEditText.setText(null);
        occupationEditText.setText(null);
        firstNameEditText.requestFocus();
    }

}
