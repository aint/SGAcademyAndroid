package com.github.aint.lesson5.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aint.lesson5.R;
import com.github.aint.lesson5.activity.MainActivity;
import com.github.aint.lesson5.model.Person;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.github.aint.lesson5.activity.MainActivity.DISPLAY_PERSON_ATTRIBUTE;
import static com.github.aint.lesson5.preference.SettingsFragment.TOGGLE_IMAGE_KEY;

public class ViewPagerFragment extends Fragment {

    private View rootView;
    private ViewPager mPager;

    private List<Person> persons;
    private Person person;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_view_pager, container, false);

        persons = ((MainActivity) getActivity()).getPersons();
        initPager();
        setPagerCurrentItem();

        return rootView;
    }

    private void initPager() {
        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        mPager.setAdapter(new AndroidImageAdapter());
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                person = persons.get(position);
            }
        });
    }

    private void setPagerCurrentItem() {
        String personName = getActivity().getIntent().getStringExtra(DISPLAY_PERSON_ATTRIBUTE);
        for (int i = 0; i < persons.size(); i++) {
            if ((persons.get(i).getFirstName() + persons.get(i).getLastName()).equals(personName)) {
                mPager.setCurrentItem(i);
                return;
            }
        }
    }

    public Person getCurrentPerson() {
        return person;
    }

    private class AndroidImageAdapter extends PagerAdapter {
        private View rootView;
        private Person person;

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
            rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_person_image, null);
            person = persons.get(position);

            togglePersonImage();

            container.addView(rootView, 0);
            return rootView;
        }

        private void togglePersonImage() {
            if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(TOGGLE_IMAGE_KEY, false)) {
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

}
