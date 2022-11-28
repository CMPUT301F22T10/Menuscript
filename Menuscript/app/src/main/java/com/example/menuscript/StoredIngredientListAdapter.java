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
/**
 * Custom ArrayAdapter for displaying the StoredIngredient objects.
 * storedIngredients {@link ArrayList<StoredIngredient>}
 *
 * @see RecipeListActivity
 * @see RecipeListAdapter
 * @see ViewRecipeActivity
 */
public class StoredIngredientListAdapter extends ArrayAdapter {
    private ArrayList<StoredIngredient> items;
    private Context context;

    public StoredIngredientListAdapter(Context context, ArrayList<StoredIngredient> items) {
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
        TextView amountText = view.findViewById(R.id.tv_item_amount);
        TextView unitText = view.findViewById(R.id.tv_item_unit);

        descriptionText.setText(ingredient.getDescription());
        amountText.setText(String.valueOf(ingredient.getAmount()));
        unitText.setText(ingredient.getUnit());

        return view;
    }
}
