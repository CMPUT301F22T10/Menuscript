package com.example.menuscript;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
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

/**
 * This class displays a list of ingredients that are currently in the meal plan.
 * Clicking on an ingredient shows some of the details relevant to shopping.
 * User can check off multiple ingredients and click the confirm button on the bottom
 * to
 *
 * @author Josh
 */
public class ShopListActivity extends AppCompatActivity {
    ArrayAdapter<Ingredient> shoppingAdapter;
    ArrayList<Ingredient> ingredientList;
    ArrayList<Ingredient> ingredientNeededList;
    ArrayList<String> ingredientNameList;
    ListView shoppingList;
    Ingredient selectedIngredient;
    private FirebaseFirestore databaseInstance;
    private CollectionReference mealPlanCollection;
    private CollectionReference ingredientCollection;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoplist_main);

        ingredientList = new ArrayList<Ingredient>();
        ingredientNeededList = new ArrayList<Ingredient>();
        ingredientNameList = new ArrayList<String>();
        shoppingList = findViewById(R.id.shopListMainIngredients);
        sortButton = findViewById(R.id.shopListMainSpinner);
        shoppingAdapter = new ShopListAdapter(this, ingredientList);
        shoppingList.setAdapter(shoppingAdapter);
        databaseInstance = FirebaseFirestore.getInstance();
        mealPlanCollection = databaseInstance.collection("MealPlanIngredients");
        ingredientCollection = databaseInstance.collection("StoredIngredients");

        mealPlanCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                ingredientNameList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String name = (String) doc.getData().get("description");

                    // TESTING
//                    float amount = Float.parseFloat(String.valueOf((doc.getData().get("amount"))));
//                    ingredientNeededList.add(new Ingredient(name, amount, "", ""));

                    ingredientNameList.add(name);
                }
                shoppingAdapter.notifyDataSetChanged();
            }
        });

        ingredientCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                ingredientList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String ingredientName = (String) doc.getData().get("description");
                    if (ingredientNameList.contains(ingredientName)) {
                        String name = (String) doc.getData().get("description");
                        String unit = (String) doc.getData().get("unit");
                        float servings = Float.parseFloat(String.valueOf((doc.getData().get("amount"))));

                        // TESTING
//                        for(int i = 0; i < ingredientNeededList.size(); i++) {
//                            if(ingredientNeededList.get(i).getDescription().equals(ingredientName)) {
//                                float amount = servings - ingredientNeededList.get(i).getAmount();
//                                break;
//                            }
//                        }

                        String category = (String) doc.getData().get("category");
                        ingredientList.add(new Ingredient(name, servings, unit, category));
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
                } else if(dapterView.getItemAtPosition(i)=="Description"){
                    ingredientList.sort(Comparator.comparing(Ingredient::getDescription));
                }
                shoppingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        FloatingActionButton fab = findViewById(R.id.shopListButton);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(ShoppingListActivity.this, AddIngredientActivity.class);
            activityResultLauncher.launch(intent);
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode()!= null) {
                Intent data = result.getData();
                String description = intent.getStringExtra("description");
                float amount = intent.getFloatExtra("amount", 0.0f);
                String unit = intent.getStringExtra("unit");
                String date = intent.getStringExtra("date");
                String category = intent.getStringExtra("category");
                String location = intent.getStringExtra("location");
                if (result.getResultCode() == 400) {
                    StoredIngredient newIngredient = new StoredIngredient(description, amount, unit, category, date, location);
                    db.addStoredIngredient(newIngredient);
                    ingredients.add(newIngredient);
                    ingredientAdapter.notifyDataSetChanged();
                    Ingredient shoppingItem = new Ingredient(description, amount, unit, category);
                    shoppingItems.add(shoppingItem);
                    shoppingAdapter.notifyDataSetChanged();
                    db.addShoppingItem(shoppingItem);
                }
            }
        });

    //As a meal planner, I want to note that I have picked up an ingredient on the shopping list.
    //As a meal planner, I want to be able to sort the shopping list by category or description.



    }
}
