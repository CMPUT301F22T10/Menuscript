package com.example.menuscript;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewShopListIngredientActivity extends AppCompatActivity {

    private TextView ingredientName;
    private EditText ingredientCount;
    private EditText ingredientUnit;
    private EditText ingredientCategory;
    private StoredIngredient selectedIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoplist_ingredient);

        Bundle bundle = getIntent().getExtras();

        ingredientName = findViewById(R.id.shopListIngredientName);
        ingredientCount = findViewById(R.id.countEditText);
        ingredientUnit = findViewById(R.id.unitEditText);
        ingredientCategory = findViewById(R.id.categoryEditText);

        ingredientName.setText(bundle.getString("NAME"));
        ingredientCount.setText(Float.toString(bundle.getFloat("AMOUNT")));
        ingredientUnit.setText(bundle.getString("CATEGORY"));
        ingredientCategory.setText(bundle.getString("UNIT"));
    }
}
