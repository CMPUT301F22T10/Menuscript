package com.example.menuscript;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * MainMenuActvitiy is the starting screen of Menuscript. The user can go to the
 * other activities, Ingredients, Recipes, Meal Plans, and Shopping List.
 * This screen is the main navigation page for users to explore all
 * functionalities of the application.
 *
 * @author Micheal,  Wanlin
 * @see IngredientListActivity
 * @see RecipeListActivity
 */
public class MainMenuActivity extends AppCompatActivity {

    //DatabaseManager db = new DatabaseManager();
    String days;

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
        days = "0";

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

        viewShoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShoppingListActivity();
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
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}

