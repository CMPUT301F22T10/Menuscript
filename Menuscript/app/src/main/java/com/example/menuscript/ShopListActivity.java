package com.example.menuscript;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * This class displays a list of ingredients that are currently in the meal plan.
 * Clicking on an ingredient shows some of the details relevant to shopping.
 * User can check off multiple ingredients and click the confirm button on the bottom
 *
 * Requires being passed hashmap of ingredients (key, amount) needed from previous activity (Main)
 *
 * @author Josh, Wanlin
 */
public class ShopListActivity extends AppCompatActivity {

    private Button addIngredientsButton;
    private ArrayList<Ingredient> ingredientToBeAddedList;
    ShopListAdapter shoppingAdapter;
    ArrayList<Ingredient> ingredientList;
    HashMap<String,Ingredient> storedIngredientsList;
    ArrayList<String> ingredientNameList;
    HashMap<String, Float> mealPlanIngredients; // {hashmap of ingredient key, amount needed}
    HashMap<String,Float> mealPlanRecipes; // {hashmap of recipe key, servings needed}



    ListView shoppingList;
    Ingredient selectedIngredient;

    private FirebaseFirestore databaseInstance;
    private CollectionReference mealPlanIngredientsCollection;
    private CollectionReference mealPlanRecipesCollection;
    private CollectionReference storedIngredientCollection;
    private CollectionReference recipesCollection;
    private Spinner sortSpinner;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoplist_main);

        ingredientList = new ArrayList<Ingredient>();
        ingredientNameList = new ArrayList<String>();

        Bundle args = getIntent().getBundleExtra("MEALPLAN_ITEMS");
        if (args.getSerializable("MEALPLAN_INGREDIENTS") != null){
            mealPlanIngredients = (HashMap<String, Float>) args.getSerializable("MEALPLAN_INGREDIENTS");
        } else{
            mealPlanIngredients = new HashMap<>();
        }

        if (args.getSerializable("MEALPLAN_RECIPES") != null){
            mealPlanRecipes  = (HashMap<String, Float>) args.getSerializable("MEALPLAN_RECIPES");
        } else{
            mealPlanRecipes  = new HashMap<>();
        }

        if (args.getSerializable("STORED_INGREDIENTS") != null){
            storedIngredientsList  = (HashMap<String,Ingredient>) args.getSerializable("STORED_INGREDIENTS");
        } else{
            storedIngredientsList  = new HashMap<>();
        }

        shoppingList = findViewById(R.id.shopListMainIngredients);
        sortSpinner = findViewById(R.id.shopListMainSpinner);
        shoppingAdapter = new ShopListAdapter(this, ingredientList);
        shoppingList.setAdapter(shoppingAdapter);
        databaseInstance = FirebaseFirestore.getInstance();
        recipesCollection = databaseInstance.collection("Recipes");


        for (String key : mealPlanIngredients.keySet()){
            if (storedIngredientsList.keySet().contains(key)){
                Ingredient ingredientToAdd = storedIngredientsList.get(key);
                Float amountNeeded = mealPlanIngredients.get(key) - ingredientToAdd.getAmount();
                ingredientToAdd.setAmount(amountNeeded);
                ingredientList.add(ingredientToAdd);
            }
        }


        recipesCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.d("SHOPPING LIST","ONEVENT RECIPES");
                for(QueryDocumentSnapshot doc: value) {
                    if (mealPlanRecipes.keySet().contains(doc.getId())) {
                        Float servings = mealPlanRecipes.get(doc.getId());

                        ArrayList<HashMap<String, String>> recipeIngredientsList = new ArrayList<>();
                        recipeIngredientsList = (ArrayList<HashMap<String, String>>) doc.get("ingredients");
                        ArrayList<Ingredient> ingredients = new ArrayList<>();

                        for (HashMap<String, String> ingredient : recipeIngredientsList) {
                            Boolean alreadyInIngredientList = false;
                            String description = String.valueOf(ingredient.get("description"));
                            String unit = String.valueOf(ingredient.get("unit"));
                            String category = String.valueOf(ingredient.get("category"));

                            for (Ingredient storedIngredient : ingredientList){ //checking if already in IngredientList
                                Log.d("SHOPPING LISTINGRE2", String.valueOf(storedIngredient.getDescription().equals(description)) + description);
                                if ((storedIngredient.getDescription().equals(description)) && (storedIngredient.getCategory().equals(category)) && (storedIngredient.getUnit().equals(unit))){
                                    float neededAmount = storedIngredient.getAmount() + (servings * Float.parseFloat(String.valueOf(ingredient.get("amount"))));
                                    storedIngredient.setAmount(neededAmount);
                                    alreadyInIngredientList = true;
                                }
                            }

                            if (!alreadyInIngredientList) {
                                //check if in storedIngredientsList
                                boolean inStoredIngredientsList = false;
                                for (String key : storedIngredientsList.keySet()){
                                    Ingredient storedIngredient = storedIngredientsList.get(key);
                                    if ((storedIngredient.getDescription().equals(description)) && (storedIngredient.getCategory().equals(category)) && (storedIngredient.getUnit().equals(unit))){
                                        Float amountNeeded = (Float.parseFloat(String.valueOf(ingredient.get("amount"))) * servings) - storedIngredient.getAmount();
                                        storedIngredient.setAmount(amountNeeded);
                                        ingredientList.add(storedIngredient);
                                        inStoredIngredientsList = true;
                                    }

                                }

                                if (!inStoredIngredientsList) {
                                    ingredientList.add(new Ingredient(
                                            description,
                                            (Float.parseFloat(String.valueOf(ingredient.get("amount"))) * servings),
                                            unit,
                                            category));
                                    alreadyInIngredientList = false;
                                }

                            }
                        }
                    }
                }
                shoppingAdapter.notifyDataSetChanged();
            }
        });

        shoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ShopListActivity.this, ViewShopListIngredientActivity.class);
                selectedIngredient = shoppingAdapter.getItem(i);
                intent.putExtra("NAME", selectedIngredient.getDescription());
                intent.putExtra("AMOUNT", selectedIngredient.getAmount());
                intent.putExtra("UNIT", selectedIngredient.getUnit());
                intent.putExtra("CATEGORY", selectedIngredient.getCategory());

                startActivity(intent);
            }
        });


        //sort button
        Spinner sortButton=findViewById(R.id.shopListMainSpinner);
        String sortOptions[]={"Sort by Category", "Sort by Description"};
        CustomSortAdapter<String> sortAdapter=new CustomSortAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, sortOptions);
        sortButton.setAdapter(sortAdapter);
        sortButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i)=="Category") {
                    ingredientList.sort(Comparator.comparing(Ingredient::getCategory));
                } else if(adapterView.getItemAtPosition(i)=="Description"){
                    ingredientList.sort(Comparator.comparing(Ingredient::getDescription));
                }
                shoppingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

/*
        addIngredientsButton = findViewById(R.id.shopListButton);
        addIngredientsButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ViewIngredientActivity.class);

            for ( Ingredient ingredient : ingredientToBeAddedList)
            intent.putExtra("INGREDIENT", ingredient);
            intent.putExtra("CATEGORIES", catOptions);
            intent.putExtra("LOCATIONS", locOptions);
            intent.putExtra("UNITS", unitOptions);
            activityResultLauncher.launch(intent);
            activityResultLauncher.launch(intent);
        });
*/




    }
}
