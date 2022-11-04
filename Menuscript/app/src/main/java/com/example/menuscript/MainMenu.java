package com.example.menuscript;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;



public class MainMenu extends AppCompatActivity {

    DatabaseManager db = new DatabaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        final Button viewIngredientsButton = findViewById(R.id.ingredient_button);
        final Button viewRecipesButton = findViewById(R.id.recipe_button);
        final Button viewMealPlanButton = findViewById(R.id.meal_plan_button);
        final Button viewShoppingListButton = findViewById(R.id.shopping_list_button);


        viewIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIngredientListActivity();
            }
        });

        viewRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecipeListActivity();
            }
        });
    }
    public void startIngredientListActivity(){
        Intent intent = new Intent(this, IngredientListActivity.class);
        startActivity(intent);
    }

    public void startRecipeListActivity(){
        Intent intent = new Intent(this, RecipeListActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}

