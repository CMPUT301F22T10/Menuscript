package com.example.menuscript;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class displays a list of ingredients.:
 * ingredientList {@link ListView}
 * ingredientAdapter {@link StoredIngredientListAdapter}
 * dataList {@link ArrayList<StoredIngredient>}
 *
 */
public class IngredientListActivity extends AppCompatActivity {

    ListView ingredientList;
    StoredIngredientListAdapter ingredientAdapter;
    ArrayList<StoredIngredient> dataList;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    DatabaseManager db = new DatabaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        ingredientList = findViewById(R.id.item_list);

        dataList = new ArrayList<>();
        dataList.addAll(db.getStoredIngredients());
        //Log.d("INGREDIENT LIST", dataList.get(0).getDate());

//        StoredIngredient test1 = new StoredIngredient( "Asparagus", 12, "pounds", "Vegetable", "05/11/2022", "fridge");
//        StoredIngredient test2 = new StoredIngredient("ThisIsToTestVeryLongCharacterStringsLikeReallyReallyReallyLongOnesIsThisLongEnough?", 12, "unit", "TestReallyLongCategories", "05/11/2023", "ThisIsAReallyLongLocation");
//        StoredIngredient test3 = new StoredIngredient("Jasmine Rice", 12, "pounds", "Carb", "01/12/2030", "cellar");

//        dataList.add(test1);
//        dataList.add(test2);
//        dataList.add(test3);

        ingredientAdapter = new StoredIngredientListAdapter(this, dataList);
        //ingredientAdapter.notifyDataSetChanged();

        ingredientList.setAdapter(ingredientAdapter);

        Spinner sortButton = findViewById(R.id.sort_button);
        String[] sortOptions = new String[]{"Description", "Best Before Date", "Category"};
        CustomSortAdapter<String> sortAdapter = new CustomSortAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sortOptions);
        sortButton.setAdapter(sortAdapter);

        ingredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(IngredientListActivity.this, ViewIngredientActivity.class);
                StoredIngredient clickedIngredient = (StoredIngredient) ingredientAdapter.getItem(i);
                intent.putExtra("INGREDIENT", clickedIngredient);
                startActivity(intent);
            }
        });



        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == 6969 && result.getData() != null) {
                    Intent intent = result.getData();
                    String description = intent.getStringExtra("description");
                    String category = intent.getStringExtra("category");
                    Integer amount = intent.getIntExtra("amount", 0);
                    String date = intent.getStringExtra("date");
                    String location = intent.getStringExtra("location");

                    StoredIngredient newIngredient = new StoredIngredient(description, amount, null, category, date, location);
                    dataList.add(newIngredient);
                    ingredientAdapter.notifyDataSetChanged();
                }
            }
        });

        FloatingActionButton addIngredientButton = findViewById(R.id.add_item_button);
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add ingredient activity
                Intent intent = new Intent(getApplicationContext(), AddIngredientActivity.class);
                activityResultLauncher.launch(intent);
            }
        });


        sortButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i) == "Category") {
                    dataList.sort(Comparator.comparing(Ingredient::getCategory));
                } else if (adapterView.getItemAtPosition(i) == "Description") {
                    dataList.sort(Comparator.comparing(Ingredient::getDescription));
                }
                ingredientAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
