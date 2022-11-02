package com.example.menuscript;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * @author Dylan Clarke
 */
public class RecipeActivity extends AppCompatActivity {

    Button addRecipeButton;
    ListView recipeList;
    ArrayList<Recipe> dataList;
    ArrayAdapter<Recipe> recipeAdapter;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_view);

        recipeList = findViewById(R.id.recipe_list);

        dataList = new ArrayList<>();

        //COMMENTED OUT BITMAP FUNCTIONALITY FOR THE TEST BELOW, SO DONT BE SURPRISED IF IT DOESNT WORK AFTER
        //I BELIEVE THESE INGREDIENTS HAVE TEMP FUNCTIONALITY AS WELL SO CHECK THEM AS WELL IF STUFF BREAKING
        Ingredient testIngredient = new Ingredient(0,"TEST","test2");
        ArrayList<Ingredient> testIngList = new ArrayList<Ingredient>();
        testIngList.add(testIngredient);
        dataList.add(new Recipe(1,"CheeseySauce",10,3,"CAT1","comment",testIngList));
        dataList.add(new Recipe(1,"CheeseySaucey",10,544,"CAT2","comment",testIngList));

        recipeAdapter = new RecipeList(this,dataList);
        recipeList.setAdapter(recipeAdapter);


        addRecipeButton = findViewById(R.id.addRecipeButton);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddRecipeActivity.class);
                startActivity(intent);
            }
        });

    }
}
