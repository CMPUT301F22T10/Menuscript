package com.example.menuscript;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * MainMenuActvitiy is the starting screen of Menuscript. The user can go to the
 * other activities, Ingredients, Recipes, Meal Plans, and Shopping List.
 * This screen is the main navigation page for users to explore all
 * functionalities of the application.
 *
 * @author Micheal,  Wanlin
 * @see IngredientListActivity
 * @see RecipeListActivity
 * @see MealPlanActivity
 * @see ShopListActivity
 */
public class MainMenuActivity extends AppCompatActivity {

    //DatabaseManager db = new DatabaseManager();
    String days;
    HashMap<String, Float> mealPlanIngredients; // {hashmap of ingredient key, amount needed}
    HashMap<String,Float> mealPlanRecipes; // {hashmap of recipe key, servings needed}
    HashMap<String,StoredIngredient> storedIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        final Button viewIngredientsButton = findViewById(R.id.ingredient_button);
        final Button viewRecipesButton = findViewById(R.id.recipe_button);
        final Button viewMealPlanButton = findViewById(R.id.meal_plan_button);
        final Button viewShoppingListButton = findViewById(R.id.shopping_list_button);
        FirebaseFirestore databaseInstance;
        CollectionReference daysCollectionReference;
        CollectionReference mealPlanIngredientsCollection;
        CollectionReference mealPlanRecipesCollection;
        CollectionReference ingredientsCollection;
        days = "0";
        mealPlanIngredients = new HashMap<>();
        mealPlanRecipes = new HashMap<>();
        storedIngredients = new HashMap<>();

        /**
         * Switches to the ingredient list activity.
         *
         * @see IngredientListActivity
         */
        viewIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIngredientListActivity();
            }
        });

        /**
         * Switches to the recipe list activity.
         *
         * @see RecipeListActivity
         */
        viewRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecipeListActivity();
            }
        });
        /**
         * Switches to the meal plan activity.
         *
         * @see MealPlanActivity
         */
        viewMealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMealPlanActivity();
            }
        });

        /**
         *getting meal plan days for meal plan activity
         */
        databaseInstance = FirebaseFirestore.getInstance();
        daysCollectionReference = databaseInstance.collection("MealPlanDays");

        daysCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc: value){
                    days = (String) doc.getData().get("days");
                }
            }
        });


        /**
         * Switches to the shopping list activity.
         *
         * @see ShopListActivity
         */
        viewShoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShoppingListActivity();
            }
        });
        /**
         * getting meal plan recipes/ingredients for shopping list
         * */

        mealPlanIngredientsCollection = databaseInstance.collection("MealPlanIngredients");
        mealPlanRecipesCollection = databaseInstance.collection("MealPlanRecipes");
        ingredientsCollection = databaseInstance.collection("StoredIngredients");

        mealPlanIngredientsCollection.addSnapshotListener((new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                mealPlanIngredients.clear();
                for(QueryDocumentSnapshot doc: value){
                    Float amount = Float.parseFloat((String) doc.getData().get("amount"));
                    String key = doc.getId();
                    mealPlanIngredients.put(key, amount);
                }
            }
        }));

        mealPlanRecipesCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                mealPlanRecipes.clear();
                for(QueryDocumentSnapshot doc: value){
                    Float amount = Float.parseFloat((String) doc.getData().get("servings"));
                    String key = doc.getId();
                    mealPlanRecipes.put(key, amount);
                }
            }
        });

        ingredientsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                storedIngredients.clear();
                for(QueryDocumentSnapshot doc: value){
                    String name = (String) doc.getData().get("description");
                    String unit = (String) doc.getData().get("unit");
                    String category = (String) doc.getData().get("category");
                    Float amount = Float.parseFloat( String.valueOf(doc.getData().get("amount")));
                    String date = (String) doc.getData().get("date");
                    String location = (String) doc.getData().get("location");
                    storedIngredients.put(doc.getId() , new StoredIngredient(name, amount, unit, category, date, location));
                }
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

    public void startMealPlanActivity(){

        Intent intent = new Intent(this, MealPlanActivity.class);
        intent.putExtra("Days", days);
        startActivity(intent);
    }

    public void startShoppingListActivity(){
        Intent intent = new Intent(this, ShopListActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("MEALPLAN_INGREDIENTS", mealPlanIngredients);
        args.putSerializable("MEALPLAN_RECIPES", mealPlanRecipes);
        args.putSerializable("STORED_INGREDIENTS", storedIngredients);
        intent.putExtra("MEALPLAN_ITEMS", args);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}

