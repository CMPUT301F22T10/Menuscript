package com.example.menuscript;

import static android.content.ContentValues.TAG;

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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class displays a list of ingredients:
 * ingredientList {@link ListView}
 * ingredientAdapter {@link StoredIngredientListAdapter}
 * ingredients {@link ArrayList<StoredIngredient>}
 *
 */
public class IngredientListActivity extends AppCompatActivity {

    ListView ingredientList;
    StoredIngredientListAdapter ingredientAdapter;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    DatabaseManager db = new DatabaseManager(this);

    private String descriptionFieldStr = "description";
    private String amountFieldStr = "amount";
    private String unitFieldStr = "unit";
    private String categoryFieldStr = "category";
    private String dateFieldStr = "date";
    private String locationFieldStr = "location";
    ArrayList<StoredIngredient> ingredients;
    private FirebaseFirestore databaseInstance;
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        ingredientList = findViewById(R.id.item_list);

        databaseInstance = FirebaseFirestore.getInstance();
        collectionReference = databaseInstance.collection("StoredIngredients");
        ingredients = new ArrayList<>();

        ingredientAdapter = new StoredIngredientListAdapter(this, ingredients);

        ingredientList.setAdapter(ingredientAdapter);

        Spinner sortButton = findViewById(R.id.sort_button);
        String[] sortOptions = new String[]{"Description", "Best Before Date", "Category"};
        CustomSortAdapter<String> sortAdapter = new CustomSortAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sortOptions);
        sortButton.setAdapter(sortAdapter);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                ingredients.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String description = (String) doc.getData().get(descriptionFieldStr);
                    float amount = Float.parseFloat(String.valueOf(doc.getData().get(amountFieldStr)));
                    String unit = (String) doc.getData().get(unitFieldStr);
                    String category = (String) doc.getData().get(categoryFieldStr);
                    String date = (String) doc.getData().get(dateFieldStr);
                    String location = (String) doc.getData().get(locationFieldStr);

                    ingredients.add(new StoredIngredient(description, amount, unit, category, date, location));
                }
                ingredientAdapter.notifyDataSetChanged();
            }
        });

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
                    db.addStoredIngredient(newIngredient);
                    ingredients.add(newIngredient);
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
                    ingredients.sort(Comparator.comparing(Ingredient::getCategory));
                } else if (adapterView.getItemAtPosition(i) == "Description") {
                    ingredients.sort(Comparator.comparing(Ingredient::getDescription));
                }
                ingredientAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
