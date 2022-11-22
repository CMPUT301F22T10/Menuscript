package com.example.menuscript;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewShopListIngredientActivity extends AppCompatActivity {

    private TextView ingredientName;
    private EditText ingredientCount;
    private EditText ingredientUnit;
    private EditText ingredientCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoplist_ingredient);

        Bundle bundle = getIntent().getExtras();

        ingredientName = findViewById(R.id.shopListIngredientName);
        ingredientCount = findViewById(R.id.countEditText);
        ingredientUnit = findViewById(R.id.unitEditText);
        ingredientCategory = findViewById(R.id.categoryEditText);

        if (bundle.getString("NAME")!= null) {
            ingredientName.setText(bundle.getString("NAME"));
        }
        ingredientCount.setText(Float.toString(bundle.getFloat("AMOUNT")));
        ingredientUnit.setText(bundle.getString("CATEGORY"));
        ingredientCategory.setText(bundle.getString("UNIT"));

    }
}
