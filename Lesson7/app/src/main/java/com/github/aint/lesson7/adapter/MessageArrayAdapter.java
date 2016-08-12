package com.github.aint.lesson7.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        }
        this.position = position;
        this.convertView = convertView;

        setUpMessage();
        
        return convertView;
    }

    private void setUpMessage() {
        ((TextView) convertView.findViewById(R.id.title)).setText(messages.get(position).getTitle());
        ((TextView) convertView.findViewById(R.id.body)).setText(messages.get(position).getBody());
    }

}
