package com.github.aint.lesson5.fragment.adapter;

import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aint.lesson5.R;
import com.github.aint.lesson5.model.Person;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.github.aint.lesson5.preference.SettingsFragment.TOGGLE_IMAGE_KEY;

public class PersonPagerAdapter extends PagerAdapter {

    private final Activity context;
    private final List<Person> persons;

    private View rootView;
    private Person person;

    public PersonPagerAdapter(Activity context, List<Person> persons) {
        this.context = context;
        this.persons = persons;
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        container.removeView((RelativeLayout) obj);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        rootView = context.getLayoutInflater().inflate(R.layout.fragment_person_image, null);
        person = persons.get(position);

        togglePersonImage();

        container.addView(rootView, 0);
        return rootView;
    }

    private void togglePersonImage() {
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(TOGGLE_IMAGE_KEY, false)) {
            displayPersonImage();
        } else {
            displayPersonName();
        }
    }

    private void displayPersonName() {
        setImageViewVisibility(false);
        setFullNameTextViewsVisible(true);
        setFullNameTextViews();
    }

    private void displayPersonImage() {
        setFullNameTextViewsVisible(false);
        setImageViewVisibility(true);
        setPersonImage();
    }

    private void setPersonImage() {
        ImageView personImg = (ImageView) rootView.findViewById(R.id.personImage);
        personImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        personImg.setImageResource(person.getImageId());
    }

    private void setImageViewVisibility(boolean flag) {
        rootView.findViewById(R.id.personImage).setVisibility(flag ? VISIBLE : GONE);
    }

    private void setFullNameTextViews() {
        ((TextView) rootView.findViewById(R.id.firstNameTextView)).setText(person.getFirstName());
        ((TextView) rootView.findViewById(R.id.lastNameTextView)).setText(person.getLastName());
    }

    private void setFullNameTextViewsVisible(boolean flag) {
        rootView.findViewById(R.id.firstNameTextView).setVisibility(flag ? VISIBLE : GONE);
        rootView.findViewById(R.id.lastNameTextView).setVisibility(flag ? VISIBLE : GONE);
    }

}
