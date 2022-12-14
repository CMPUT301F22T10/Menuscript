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
 * Custom array adapter for recipe list in RecipeListActivity
 * recipes {@link ArrayList<Recipe>}
 *
 * @author Wanlin
 * @see Recipe
 */
public class RecipeListAdapter extends ArrayAdapter<Recipe> {
    private ArrayList<Recipe> recipes;
    private Context context;

    public RecipeListAdapter(Context context, ArrayList<Recipe>  recipes){
        super(context, 0,recipes);
        this.recipes = recipes;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.recipe_ingredient_content,parent,false);
        }
        Recipe recipe = recipes.get(position);

        TextView recipeName = view.findViewById(R.id.description_text);
        //TextView tv_amount = view.findViewById(R.id.tv_item_amount);
        //TextView tv_unit = view.findViewById(R.id.tv_item_unit);

        recipeName.setText(recipe.getTitle());
        //tv_amount.setText(String.valueOf(recipe.))

        return view;
    }

}