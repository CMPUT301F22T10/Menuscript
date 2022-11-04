package com.example.menuscript;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Custom ArrayAdapter for sorting objects.
 *
 * @param <T> Filler parameter.
 */
public class CustomSortAdapter<T> extends ArrayAdapter<String> {
    public CustomSortAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText("");
        return view;
    }

}
