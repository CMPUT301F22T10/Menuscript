package com.example.menuscript;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;

public class EditIngredientInRecipeActivity extends AppCompatActivity implements AddOptionFragment.OnFragmentInteractionListener {

    private EditText ingredientDescription;
    private EditText ingredientAmount;
    private Spinner ingredientUnit;
    private Spinner ingredientCategory;

    ArrayAdapter<String> catAdapter;
    ArrayList<String> catOptions;

    ArrayAdapter<String> unitAdapter;
    ArrayList<String> unitOptions;

    private FirebaseFirestore databaseInstance;
    private CollectionReference collectionReference;
    DatabaseManager db = new DatabaseManager(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_ingredientedit);

        databaseInstance = FirebaseFirestore.getInstance();
        collectionReference = databaseInstance.collection("Options");

        ingredientDescription = findViewById(R.id.itemDescriptionEditText_recipe);
        ingredientAmount = findViewById(R.id.countEditText_recipe);
        ingredientUnit = findViewById(R.id.unitSpinner_recipe);
        ingredientCategory = findViewById(R.id.categorySpinner_recipe);


        catOptions = (ArrayList<String>) getIntent().getSerializableExtra("CATEGORIES");
        unitOptions = (ArrayList<String>) getIntent().getSerializableExtra("UNITS");

        catAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catOptions);
        unitAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unitOptions);

        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ingredientCategory.setAdapter(catAdapter);
        ingredientUnit.setAdapter(unitAdapter);

        Ingredient viewedIngredient = (Ingredient) getIntent().getSerializableExtra("INGREDIENT");

        ingredientDescription.setText(viewedIngredient.getDescription());
        ingredientAmount.setText((String.valueOf(viewedIngredient.getAmount())));

        if (viewedIngredient.getCategory() != null && catOptions.contains(viewedIngredient.getCategory())) {
            ingredientCategory.setSelection(catOptions.indexOf(viewedIngredient.getCategory()));
        } else if (!catOptions.contains(viewedIngredient.getCategory())) {
            catOptions.add(0, viewedIngredient.getCategory());
            catAdapter.notifyDataSetChanged();
            ingredientCategory.setSelection(0);
        } else {
            catOptions.add(0, "");
            catAdapter.notifyDataSetChanged();
            ingredientCategory.setSelection(0);
        }

        if (viewedIngredient.getUnit() != null && unitOptions.contains(viewedIngredient.getUnit())) {
            ingredientUnit.setSelection(unitOptions.indexOf(viewedIngredient.getUnit()));
        } else if (!unitOptions.contains(viewedIngredient.getUnit())) {
            unitOptions.add(0, viewedIngredient.getCategory());
            unitAdapter.notifyDataSetChanged();
            ingredientUnit.setSelection(0);
        } else {
            unitOptions.add(0, "");
            unitAdapter.notifyDataSetChanged();
            ingredientUnit.setSelection(0);
        }


        Button submitButton = findViewById(R.id.submitButton_recipe);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                onButtonClick(intent);
                setResult(401, intent);
                finish();

            }
        });

        Button deleteButton = findViewById(R.id.deleteButton_recipe);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                onButtonClick(intent);
                setResult(402, intent);
                finish();

            }
        });
    }

    private Intent onButtonClick(Intent intent) {

        if (!ingredientDescription.getText().toString().equals("")) {
            intent.putExtra("description", ingredientDescription.getText().toString());
        } else {
            intent.putExtra("description", "Unnamed Ingredient");
        }
        if (!ingredientAmount.getText().toString().equals("")) {
            intent.putExtra("amount", Float.valueOf(ingredientAmount.getText().toString()));
        } else {
            intent.putExtra("amount", 0.0f);
        }
        if (!ingredientUnit.getSelectedItem().toString().equals("")) {
            intent.putExtra("unit", ingredientUnit.getSelectedItem().toString());
        } else {
            intent.putExtra("unit", "No Unit");
        }
        if (!ingredientCategory.getSelectedItem().toString().equals("")) {
            intent.putExtra("category", ingredientCategory.getSelectedItem().toString());
        } else {
            intent.putExtra("category", "Uncategorized");
        }
        return intent;
    }

    /**
     * Handles returning from AddOptionFragment when adding a new category, location, or unit
     *
     * @param option
     * @param tag
     */
    public void onAddOKPressed(String option, int tag) {
        if (option != null) {
            if (tag == 1) {
                db.addIngredientCategory(option);
                catOptions.remove("");
                catOptions.add(0, option);
                catAdapter.notifyDataSetChanged();
                ingredientCategory.setSelection(0);
            } else if (tag == 3) {
                db.addLocation(option);
                unitOptions.remove("");
                unitOptions.add(0, option);
                unitAdapter.notifyDataSetChanged();
                ingredientUnit.setSelection(0);
            }
        }
    }
}

