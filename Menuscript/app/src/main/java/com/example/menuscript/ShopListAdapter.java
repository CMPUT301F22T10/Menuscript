package com.example.menuscript;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Custom array adapter for shopping list in ShopListActivity
 * mealPlanIngredients {@link ArrayList<Ingredient>}
 * checkedIngredients {@link ArrayList<Ingredient>}
 */
public class ShopListAdapter extends ArrayAdapter<Ingredient> {
    private Context context;
    private ArrayList<Ingredient> mealPlanIngredients;
    private ArrayList<Ingredient> checkedIngredients;


    public ShopListAdapter(Context context, ArrayList<Ingredient> mealPlanIngredients) {
        super(context, 0, mealPlanIngredients);
        this.context = context;
        this.mealPlanIngredients = mealPlanIngredients;
        this.checkedIngredients = new ArrayList<Ingredient>();
    }

    public ArrayList<Ingredient> getIngredients() {
        return checkedIngredients;
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
        checkbox.setChecked(false);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkbox.isChecked()) {
                    checkedIngredients.add(ingredient);
                }
            }
        });

        return view;
    }
}
