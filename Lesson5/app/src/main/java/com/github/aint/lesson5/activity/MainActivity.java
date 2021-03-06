package com.github.aint.lesson5.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.github.aint.lesson5.R;
import com.github.aint.lesson5.asynctask.ReadPersonsFromPrefsTask;
import com.github.aint.lesson5.fragment.PersonDetailsFragment;
import com.github.aint.lesson5.fragment.ViewPagerFragment;
import com.github.aint.lesson5.model.Person;
import com.github.aint.lesson5.preference.SettingsFragment;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    public static final String DISPLAY_PERSON_ATTRIBUTE = "display";

    private ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
    private PersonDetailsFragment personDetailsFragment = new PersonDetailsFragment();

    private List<Person> persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAllPersons();
        setNoPersonTextView();

        replaceFragment(viewPagerFragment);
    }

    private void getAllPersons() {
        try {
            persons = new ReadPersonsFromPrefsTask(this).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Log.e(TAG, sw.toString());
        }
    }

    public List<Person> getPersons() {
        return persons;
    }

    private void setNoPersonTextView() {
        LinearLayout noPersonView = (LinearLayout) findViewById(R.id.noPersonTextView);
        if (noPersonView != null) {
            noPersonView.setVisibility(persons.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    public void onExitButtonClick(View view) {
        exitApp();
    }

    public void onImageClick(View view) {
        replaceFragment(personDetailsFragment.setPerson(viewPagerFragment.getCurrentPerson()));
    }

    public void onBackButtonClick(View view) {
        replaceFragment(viewPagerFragment);
    }

    @Override
    public void onBackPressed() {
        if (checkSettingFragment()) {
            replaceFragment(viewPagerFragment);
        }
//        super.onBackPressed();
    }

    private boolean checkSettingFragment() {
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.container);
        return currentFragment instanceof SettingsFragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (R.id.menu_add == itemId) {
            startActivity(new Intent(this, AddPersonActivity.class));
        } else if (R.id.menu_setting == itemId) {
            replaceFragment(new SettingsFragment());
        } else if (R.id.menu_exit == itemId) {
            exitApp();
        }
        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void exitApp() {
        finish();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

}
