package com.example.menuscript;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomIngredientList extends ArrayAdapter {
    private ArrayList<Ingredient> items;
    private Context context;


    public CustomIngredientList(Context context, ArrayList<Ingredient> items) {
        super(context, 0, items);
        this.items = items;
        this.context = context;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.recipe_ingredient_content, parent, false);
        }
        Ingredient ingredient = items.get(position);
        TextView descriptionText = view.findViewById(R.id.description_text);

        descriptionText.setText(ingredient.getDescription());

        return view;
    }
}

