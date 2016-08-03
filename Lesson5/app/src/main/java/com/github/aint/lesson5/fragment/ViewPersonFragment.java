package com.github.aint.lesson5.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.aint.lesson5.R;
import com.github.aint.lesson5.activity.MainActivity;
import com.github.aint.lesson5.model.Person;

public class ViewPersonFragment extends Fragment {

    private Person person;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            person = (Person) getArguments().getSerializable(MainActivity.PERSON_ATTRIBUTE);
            System.out.println(person);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_person, container, false);
//        ImageView personImage = (ImageView) view.findViewById(R.id.personImage);
//        personImage.setImageDrawable(person.getId());
        return view;
    }

}
