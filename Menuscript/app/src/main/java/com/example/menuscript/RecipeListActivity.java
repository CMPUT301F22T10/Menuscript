package com.example.menuscript;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;

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
//_____________________TESTING_______________________
    ArrayList<Ingredient> ingredients;
//---------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        header = findViewById(R.id.header);
        header.setText("RECIPES");

        recipeList = findViewById(R.id.item_list);
        dataList = new ArrayList<Recipe>();
        recipeAdapter = new RecipeListAdapter(this, dataList);
        recipeList.setAdapter(recipeAdapter);
        //_____________________TESTING_______________________

        ingredients = new ArrayList<Ingredient>();
        Ingredient test1 = new Ingredient(1, "Asparagus", "Vegetable");
        Ingredient test2 = new Ingredient(2, "ThisIsToTestVeryLongCharacterStringsLikeReallyReallyReallyLongOnesIsThisLongEnough?", "TestReallyLongCategories");
        Ingredient test3 = new Ingredient(3, "Jasmine Rice", "Carb");
        ingredients.add(test1);
        ingredients.add(test2);
        ingredients.add(test3);
        Recipe recipe1 = new Recipe(1, "Yummy title", 4, (float)4, "Dinner", "yummy food for dinner", ingredients);
        Recipe recipe2 = new Recipe(1, "Delicious title", 10, (float)2, "Lunch", "delicious food for lunch wwwaaafawklglkawnglkanwg long string long string long string so many comments wowowowowowowowowowowowowowwoow", ingredients);
        dataList.add(new Recipe(1,"CheeseySauce",10,3,"CAT1","comment",ingredients));
        dataList.add(new Recipe(1,"CheeseySaucey",10,544,"CAT2","comment",ingredients));
        dataList.add(recipe1);
        dataList.add(recipe2);
        //---------------------------------------------------

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
                Bundle args = new Bundle();
                args.putSerializable("INGREDIENTS", selectedRecipe.getIngredients());
                intent.putExtra("INGREDIENTS_BUNDLE", args);

                startActivity(intent);
            }
        });

        Spinner sortButton = findViewById(R.id.sort_button);
        String[] sortOptions = new String[]{ "Title", "Preparation time", "Number of servings", "Category"};
        CustomSortAdapter<String> sortAdapter = new CustomSortAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sortOptions);
        sortButton.setAdapter(sortAdapter);

        sortButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i) == "Category") {
                    dataList.sort(Comparator.comparing(Recipe::getCategory));
                } else if (adapterView.getItemAtPosition(i) == "Title") {
                    dataList.sort(Comparator.comparing(Recipe::getTitle));
                }
                else if (adapterView.getItemAtPosition(i) == "Preparation time") {
                    dataList.sort(Comparator.comparing(Recipe::getTime));
                }
                else if (adapterView.getItemAtPosition(i) == "Number of servings") {
                    dataList.sort(Comparator.comparing(Recipe::getServings));
                }
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        FloatingActionButton addRecipeButton = findViewById(R.id.add_item_button);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add recipe activity
            }
        });
    }
}
