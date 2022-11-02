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

public class RecipeIngredientList extends ArrayAdapter<Ingredient> {
    private ArrayList<Ingredient> recipeIngredients;
    private Context context;

    public RecipeIngredientList(Context context, ArrayList<Ingredient>  recipeIngredients){
        super(context, 0,recipeIngredients);
        this.recipeIngredients = recipeIngredients;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View view = convertView;
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.recipe_name,parent,false);
        }
        Ingredient ingredient = recipeIngredients.get(position);

        TextView IngredientName = view.findViewById(R.id.recipeName);
        IngredientName.setText(ingredient.getDescription());

        return view;
    }

}
