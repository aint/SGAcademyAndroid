package com.github.aint.lesson5.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aint.lesson5.R;
import com.github.aint.lesson5.activity.MainActivity;
import com.github.aint.lesson5.model.Person;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.github.aint.lesson5.preference.SettingsFragment.TOGGLE_IMAGE_KEY;

public class PersonImageFragment extends Fragment {

    private Person person;
    private View rootView;

    public static PersonImageFragment newInstance(Person person) {
        PersonImageFragment fragment = new PersonImageFragment();
        Bundle args = new Bundle();
        args.putSerializable(MainActivity.PERSON_ATTRIBUTE, person);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            person = (Person) getArguments().getSerializable(MainActivity.PERSON_ATTRIBUTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_person_image, container, false);

        if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(TOGGLE_IMAGE_KEY, false)) {
            setFullNameTextViewsVisible(false);
            setImageViewVisibility(true);
            setPersonImage();
        } else {
            setImageViewVisibility(false);
            setFullNameTextViewsVisible(true);
            setFullNameTextViews();
        }

        return rootView;
    }

    private void setPersonImage() {
        ((ImageView) rootView.findViewById(R.id.personImage)).setImageResource(person.getImageId());
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
