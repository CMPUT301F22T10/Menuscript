package com.example.menuscript;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
 * selectedRecipe {@link Recipe}
 *
 * @author Wanlin
 */

public class RecipeListActivity extends AppCompatActivity {
    ListView recipeList;
    ArrayAdapter<Recipe> recipeAdapter;
    ArrayList<Recipe> dataList;
    TextView header;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    Recipe selectedRecipe;
//_____________________TESTING_______________________
    ArrayList<Ingredient> ingredients;
//---------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        header = findViewById(R.id.header);
        header.setText(R.string.recipes);

        recipeList = findViewById(R.id.item_list);
        dataList = new ArrayList<Recipe>();
        recipeAdapter = new RecipeListAdapter(this, dataList);
        recipeList.setAdapter(recipeAdapter);
        //_____________________TESTING_______________________

        ingredients = new ArrayList<Ingredient>();
        Ingredient test1 = new Ingredient( "Asparagus", 12, "pounds", "Vegetable");
        Ingredient test2 = new Ingredient("ThisIsToTestVeryLongCharacterStringsLikeReallyReallyReallyLongOnesIsThisLongEnough?", 12, "unit", "TestReallyLongCategories");
        Ingredient test3 = new Ingredient("Jasmine Rice", 12, "pounds", "Carb");
        ingredients.add(test1);
        ingredients.add(test2);
        ingredients.add(test3);
        Recipe recipe1 = new Recipe("Yummy title", 4, (float)4, "Dinner", "yummy food for dinner", null, ingredients);
        Recipe recipe2 = new Recipe("Delicious title", 10, (float)2, "Lunch", "delicious food for lunch wwwaaafawklglkawnglkanwg long string long string long string so many comments wowowowowowowowowowowowowowwoow", null, ingredients);
        dataList.add(new Recipe("CheeseySauce",10,3,"CAT1","comment", null,ingredients));
        dataList.add(new Recipe("CheeseySaucey",10,544,"CAT2","comment", null, ingredients));
        dataList.add(recipe1);
        dataList.add(recipe2);
        //---------------------------------------------------

        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RecipeListActivity.this, ViewRecipeActivity.class);
                selectedRecipe = recipeAdapter.getItem(i);
                intent.putExtra("NAME", selectedRecipe.getTitle());
                intent.putExtra("TIME", selectedRecipe.getTime());
                intent.putExtra("CATEGORY", selectedRecipe.getCategory());
                intent.putExtra("SERVINGS", selectedRecipe.getServings());
                intent.putExtra("COMMENTS", selectedRecipe.getComments());
                intent.putExtra("IMAGE", selectedRecipe.getImage());
                Bundle args = new Bundle();
                args.putSerializable("INGREDIENTS", selectedRecipe.getIngredients());
                intent.putExtra("INGREDIENTS_BUNDLE", args);

                //startActivity(intent);
                activityResultLauncher.launch(intent);
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

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getData() != null){
                    //Toast.makeText(RecipeListActivity.this, "TEST",Toast.LENGTH_SHORT).show();
                    Intent intent = result.getData();
                    String title = intent.getStringExtra("title");
                    int time = intent.getIntExtra("time",0);
                    float servings = intent.getFloatExtra("servings",0.0f);
                    String category = intent.getStringExtra("category");
                    String comments = intent.getStringExtra("comments");
                    byte[] image = intent.getByteArrayExtra("image"); //CORRESPONDS TO LINES IN ADDRECIPEACTIVITY
                    if(result.getResultCode() == 420) {
                        Recipe newRecipe = new Recipe(title, time, servings, category, comments, image, ingredients);
                        dataList.add(newRecipe);

                    } else if (result.getResultCode() == 421){
                        selectedRecipe.setTitle(title);
                        selectedRecipe.setTime(time);
                        selectedRecipe.setServings(servings);
                        selectedRecipe.setCategory(category);
                        selectedRecipe.setComments(comments);
                        selectedRecipe.setImage(image);
                    } else if (result.getResultCode() == 422){
                        dataList.remove(selectedRecipe);
                    }
                    recipeAdapter.notifyDataSetChanged();

                }
            }
        });

        FloatingActionButton addRecipeButton = findViewById(R.id.add_item_button);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add recipe activity
                //Bundle ingredients = new Bundle();
                Intent intent = new Intent(getApplicationContext(),AddRecipeActivity.class);
                intent.putExtra("ingredients",ingredients); //NOTE: USING TEMPORARY ingredients ARRAYLIST -- PLEASE NOTE FOR FULL IMPLEMENTATION
                activityResultLauncher.launch(intent);
            }
        });
    }
}
