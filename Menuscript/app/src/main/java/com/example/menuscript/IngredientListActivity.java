package com.example.menuscript;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class IngredientListActivity extends AppCompatActivity {

    ListView ingredientList;
    CustomIngredientList ingredientAdapter;
    ArrayList<Ingredient> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_list_activity);

        ingredientList = findViewById(R.id.ingredient_list);

        dataList = new ArrayList<>();

        Ingredient test1 = new Ingredient(1, "Asparagus", "Vegetable");

        Ingredient test2 = new Ingredient(2, "ThisIsToTestVeryLongCharacterStrings", "TestReallyLongCategories");

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

        sortButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i) == "Category") {
                    Collections.sort(dataList, new Comparator<Ingredient>() {
                        public int compare(Ingredient left, Ingredient right) {
                            return left.getCategory().compareTo(right.getCategory());
                        }
                    });
                } else if (adapterView.getItemAtPosition(i) == "Description") {
                    Collections.sort(dataList, new Comparator<Ingredient>() {
                        public int compare(Ingredient left, Ingredient right) {
                            return left.getDescription().compareTo(right.getDescription());
                        }
                    });
                }
                ingredientAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

}

