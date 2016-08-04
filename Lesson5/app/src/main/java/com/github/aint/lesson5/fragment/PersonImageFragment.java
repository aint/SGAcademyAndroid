package com.github.aint.lesson5.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.aint.lesson5.R;
import com.github.aint.lesson5.activity.MainActivity;
import com.github.aint.lesson5.model.Person;

public class PersonImageFragment extends Fragment {

    private Person person;

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
        View view = inflater.inflate(R.layout.fragment_view_person, container, false);
        setPersonImage(view);
        return view;
    }

    private void setPersonImage(View view) {
        ImageView personImage = (ImageView) view.findViewById(R.id.personImage);
        personImage.setImageResource(person.getImageId());
    }

}
