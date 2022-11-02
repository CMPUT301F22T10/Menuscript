package com.example.menuscript;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;

public class IngredientListActivity extends AppCompatActivity {

    ListView ingredientList;
    CustomIngredientList ingredientAdapter;
    ArrayList<Ingredient> dataList;

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

        FloatingActionButton addIngredientButton = findViewById(R.id.add_item_button);
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add ingredient activity
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

