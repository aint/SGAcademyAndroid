package com.github.aint.lesson2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.aint.lesson2.R;
import com.github.aint.lesson2.model.Person;

import java.util.List;

public class PersonArrayAdapter extends ArrayAdapter<Person> {

    private final List<Person> persons;
    private final LayoutInflater inflater;

    private int position;
    private View convertView;

    public PersonArrayAdapter(Context context, List<Person> persons) {
        super(context, R.layout.list_view_adapter_layout, persons);
        this.persons = persons;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_view_adapter_layout, parent, false);

            this.position = position;
            this.convertView = convertView;

            setFullName();
        }
        return convertView;
    }

    private void setFullName() {
        ((TextView) convertView.findViewById(R.id.label1)).setText(persons.get(position).getFirstName());
        ((TextView) convertView.findViewById(R.id.label2)).setText(persons.get(position).getLastName());
    }

}
