package com.github.aint.lesson7.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.github.aint.lesson7.R;
import com.github.aint.lesson7.model.Message;

import java.util.List;

public class MessageArrayAdapter extends ArrayAdapter<Message> {

    private final List<Message> messages;
    private final LayoutInflater inflater;

    private int position;
    private View convertView;

    public MessageArrayAdapter(Context context, List<Message> messages) {
        super(context, R.layout.list_view_adapter_layout, messages);
        this.messages = messages;

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
//        ((TextView) convertView.findViewById(R.id.label1)).setText(persons.get(position).getFirstName());
//        ((TextView) convertView.findViewById(R.id.label2)).setText(persons.get(position).getLastName());
    }

}
