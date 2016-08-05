package com.github.aint.lesson5.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.github.aint.lesson5.R;
import com.github.aint.lesson5.asynctask.ReadPersonsFromPrefsTask;
import com.github.aint.lesson5.fragment.PersonDetailsFragment;
import com.github.aint.lesson5.fragment.PersonImageFragment;
import com.github.aint.lesson5.model.Person;
import com.github.aint.lesson5.preference.SettingsFragment;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    public static final String PERSON_PREFS_NAME = "person_prefs";
    public static final String PERSON_KEYS = "person_keys";
    public static final String PERSON_ATTRIBUTE = "person";

    private List<Person> persons;
    private Person person;

    private ViewPager mPager;

    private PersonDetailsFragment personDetailsFragment = new PersonDetailsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPersons();
        setNoPersonTextView();

        initPager();
    }

    private void initPager() {
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new PersonImagePagerAdapter(getFragmentManager()));
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                person = persons.get(position);
                invalidateOptionsMenu();
            }
        });
    }

    private void getPersons() {
        try {
            persons = new ReadPersonsFromPrefsTask(
                    getSharedPreferences(PERSON_PREFS_NAME, Context.MODE_PRIVATE)).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Log.e(LOG_TAG, sw.toString());
        }
    }

    private void setNoPersonTextView() {
        LinearLayout noPersonView = (LinearLayout) findViewById(R.id.noPersonTextView);
        if (noPersonView != null) {
            noPersonView.setVisibility(persons.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    public void onExitButtonClick(View view) {
        finish();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void onImageClick(View view) {
        mPager.setVisibility(View.GONE);
        replaceFragment(PersonDetailsFragment.newInstance(person));
    }

    public void onBackButtonClick(View view) {
        mPager.setVisibility(View.VISIBLE);
        removeFragment(personDetailsFragment);
    }

    @Override
    public void onBackPressed() {
        if (checkSettingFragment()) {
            return;
        }

        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private boolean checkSettingFragment() {
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.container);
        if (currentFragment instanceof SettingsFragment) {
            mPager.setVisibility(View.VISIBLE);
            removeFragment(currentFragment);
            return true;
        }
        return false;
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
            mPager.setVisibility(View.GONE);
            replaceFragment(new SettingsFragment());
        } else if (R.id.menu_exit == itemId) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
    }

    private void replaceFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private class PersonImagePagerAdapter extends FragmentStatePagerAdapter {

        public PersonImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PersonImageFragment.newInstance(persons.get(position));
        }

        @Override
        public int getCount() {
            return persons.size();
        }
    }

}
