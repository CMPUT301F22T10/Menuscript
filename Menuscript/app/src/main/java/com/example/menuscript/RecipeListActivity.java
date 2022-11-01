package com.example.menuscript;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * TODO:
 * GET RECIPE CLASS
 * GET LIST XML
 */

/**
 * This class controls the view recipes list activity:
 * recipeList {@link ListView}
 * recipeAdapter {@link ArrayAdapter}
 * dataList {@link ArrayList<Recipe>}
 * header {@link TextView}
 *
 * @author Wanlin
 *
 */

public class RecipeListActivity extends AppCompatActivity {
    ListView recipeList;
    ArrayAdapter<Recipe> recipeAdapter;
    ArrayList<Recipe> dataList;
    TextView header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_list_activity);

        header = findViewById(R.id.ingredient_header);
        header.setText("RECIPES");

        recipeList = findViewById(R.id.ingredient_list);
        dataList = new ArrayList<Recipe>();
        recipeAdapter = new RecipeList(this, dataList);
        recipeList.setAdapter(recipeAdapter);

        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RecipeListActivity.this, ViewRecipeActivity.class);
                Recipe selectedRecipe = recipeAdapter.getItem(i);
                intent.putExtra("NAME", selectedRecipe.getTitle());
                intent.putExtra("TIME", selectedRecipe.getTime());
                intent.putExtra("CATEGORY", selectedRecipe.getCategory());
                intent.putExtra("SERVINGS", selectedRecipe.getServings());
                intent.putExtra("COMMENTS", selectedRecipe.getComments());
                intent.putExtra("INGREDIENTS", selectedRecipe.getIngredients());

                startActivity(intent);
            }
        });
    }
}
