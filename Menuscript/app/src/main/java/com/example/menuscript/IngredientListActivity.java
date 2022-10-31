package com.example.menuscript;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

public class IngredientListActivity extends AppCompatActivity {

    ListView ingredientList;
    CustomIngredientList ingredientAdapter;
    ArrayList<TestIngredient> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_list_activity);

        ingredientList = findViewById(R.id.ingredient_list);

        dataList = new ArrayList<>();

        TestIngredient test1 = new TestIngredient("Tuna Fish", "Protein");

        TestIngredient test2 = new TestIngredient("ThisIsToTestVeryLongCharacterStrings", "oog");

        TestIngredient test3 = new TestIngredient("Basmati Rice", "Carb", 1700, "g");

        dataList.add(test1);
        dataList.add(test2);
        dataList.add(test3);

        ingredientAdapter = new CustomIngredientList(this, dataList);

        ingredientList.setAdapter(ingredientAdapter);


    }
}

