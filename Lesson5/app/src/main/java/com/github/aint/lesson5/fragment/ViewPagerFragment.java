package com.github.aint.lesson5.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.aint.lesson5.R;
import com.github.aint.lesson5.activity.MainActivity;
import com.github.aint.lesson5.fragment.adapter.PersonPagerAdapter;
import com.github.aint.lesson5.model.Person;

import java.util.List;

import static com.github.aint.lesson5.activity.MainActivity.DISPLAY_PERSON_ATTRIBUTE;

public class ViewPagerFragment extends Fragment {

    private View rootView;
    private ViewPager mPager;

    private List<Person> persons;
    private Person person;

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
        mPager.setAdapter(new PersonPagerAdapter(getActivity(), persons));
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

}
