package com.example.menuscript;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShopListAdapter extends ArrayAdapter<Ingredient> {
    private Context context;
    private ArrayList<Ingredient> mealPlanIngredients;

    public ShopListAdapter(Context context, ArrayList<Ingredient> mealPlanIngredients) {
        super(context, 0, mealPlanIngredients);
        this.context = context;
        this.mealPlanIngredients = mealPlanIngredients;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.shoplist_maincontent, parent, false);
        }
        Ingredient ingredient = mealPlanIngredients.get(position);
        ListView listview = (ListView)parent;

        TextView IngredientName = view.findViewById(R.id.shopListMainIngredient);
        IngredientName.setText(ingredient.getDescription());

        CheckBox checkbox = (CheckBox) view.findViewById(R.id.shopListMainCheckBox);
        if(listview.getSelectedItemPosition() == position) {
            checkbox.setChecked(true);
        } else {
            checkbox.setChecked(false);
        }

        return view;
    }
}