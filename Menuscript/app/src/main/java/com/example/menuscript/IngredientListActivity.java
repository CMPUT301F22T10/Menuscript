package com.example.menuscript;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;

public class IngredientListActivity extends AppCompatActivity {

    ListView ingredientList;
    CustomIngredientList ingredientAdapter;
    ArrayList<Ingredient> dataList;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        ingredientList = findViewById(R.id.item_list);

        dataList = new ArrayList<>();

        Ingredient test1 = new Ingredient(1, "Asparagus", "Vegetable");
        Ingredient test2 = new Ingredient(2, "ThisIsToTestVeryLongCharacterStringsLikeReallyReallyReallyLongOnesIsThisLongEnough?", "TestReallyLongCategories");
        Ingredient test3 = new Ingredient(3, "Jasmine Rice", "Carb");

        dataList.add(test1);
        dataList.add(test2);
        dataList.add(test3);

        ingredientAdapter = new CustomIngredientList(this, dataList);
        ingredientAdapter.notifyDataSetChanged();

        ingredientList.setAdapter(ingredientAdapter);

        Spinner sortButton = findViewById(R.id.sort_button);
        String[] sortOptions = new String[]{"Description", "Best Before Date", "Category"};
        CustomSortAdapter<String> sortAdapter = new CustomSortAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sortOptions);
        sortButton.setAdapter(sortAdapter);


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

                    Ingredient newIngredient = new Ingredient(33, description, category);
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
