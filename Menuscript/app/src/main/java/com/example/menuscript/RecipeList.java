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
 * @author Dylan Clarke
 */
public class RecipeList extends ArrayAdapter<Recipe> {

    private ArrayList<Recipe> recipes;
    private Context context;

    public RecipeList(Context context, ArrayList<Recipe> recipes){
        super(context,0,recipes);
        this.recipes = recipes;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.recipe_maincontext,parent,false);
        }

        Recipe recipe = recipes.get(position);

        TextView recipeTitle = view.findViewById(R.id.recipe_listtitle);
        TextView recipeServings = view.findViewById(R.id.recipe_listservings);
        TextView recipeCategory = view.findViewById(R.id.recipe_listcategory);

        recipeTitle.setText(recipe.getTitle());
        recipeServings.setText("Servings: " + String.valueOf(recipe.getServings()));
        recipeCategory.setText(recipe.getCategory());

        return view;
    }
}
