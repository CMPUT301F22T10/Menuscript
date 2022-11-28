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
 * Custom ArrayAdapter for displaying the Ingredient objects in the ViewRecipeActivity.
 * recipeIngredients {@link ArrayList<Ingredient>}
 *
 * @see RecipeListActivity
 * @see RecipeListAdapter
 * @see ViewRecipeActivity
 */
public class  RecipeIngredientListAdapter extends ArrayAdapter<Ingredient> {
    private ArrayList<Ingredient> recipeIngredients;
    private Context context;

    /**
     * This is a constructor to create an adapter
     *
     * @param context The corresponding context for the view {@link Context}
     * @param recipeIngredients A list of ingredients that the recipe uses {@link ArrayList<Ingredient>}
     */

    public RecipeIngredientListAdapter(Context context, ArrayList<Ingredient>  recipeIngredients){
        super(context, 0,recipeIngredients);
        this.recipeIngredients = recipeIngredients;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View view = convertView;
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.recipe_ingredient_content,parent,false);
        }
        Ingredient ingredient = recipeIngredients.get(position);

        TextView IngredientName = view.findViewById(R.id.description_text);
        IngredientName.setText(ingredient.getDescription());

        return view;
    }

}
