package com.example.menuscript;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

/**
 * This class displays a list of ingredients:
 * ingredientList {@link ListView}
 * ingredientAdapter {@link StoredIngredientListAdapter}
 * ingredients {@link ArrayList<StoredIngredient>}
 *
 * @author Micheal
 * @see Ingredient
 * @see AddIngredientActivity
 */
public class IngredientListActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> activityResultLauncher;
    DatabaseManager db = new DatabaseManager(this);

    private final String descriptionFieldStr = "description";
    private final String amountFieldStr = "amount";
    private final String unitFieldStr = "unit";
    private final String categoryFieldStr = "category";
    private final String dateFieldStr = "date";
    private final String locationFieldStr = "location";

    ArrayList<StoredIngredient> ingredients;
    ListView ingredientList;
    StoredIngredientListAdapter ingredientAdapter;

    StoredIngredient clickedIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        FirebaseFirestore databaseInstance = FirebaseFirestore.getInstance();

        ingredientList = findViewById(R.id.item_list);

        ingredients = new ArrayList<>();
        ingredientAdapter = new StoredIngredientListAdapter(this, ingredients);
        ingredientList.setAdapter(ingredientAdapter);

        Spinner sortButton = findViewById(R.id.sort_button);
        String[] sortOptions = new String[]{"Description", "Best Before Date", "Category"};
        CustomSortAdapter<String> sortAdapter = new CustomSortAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sortOptions);
        sortButton.setAdapter(sortAdapter);

        //  fetches Ingredient list from Firestore
        CollectionReference ingredientReference = databaseInstance.collection("StoredIngredients");
        ingredientReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                ingredients.clear();
                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
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

        final String addOption = "Add new item";

        //  fetches Categories from Firestore
        ArrayList<String> catOptions = new ArrayList<>();
        CollectionReference catColRef = databaseInstance.collection("Options");
        final DocumentReference catDocRef = catColRef.document("Ingredient Categories");
        catDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    catOptions.clear();
                    catOptions.add(addOption);
                    catOptions.addAll(0, Objects.requireNonNull(snapshot.getData()).keySet());
                    Log.d("category data", "Current data: " + snapshot.getData());
                } else {
                    Log.d("category data", "Current data: null");
                }
            }
        });

        //  fetches Locations from Firestore
        ArrayList<String> locOptions = new ArrayList<>();
        CollectionReference locColRef = databaseInstance.collection("Options");
        final DocumentReference locDocRef = locColRef.document("Locations");

        locDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    locOptions.clear();
                    locOptions.add(addOption);
                    locOptions.addAll(0, Objects.requireNonNull(snapshot.getData()).keySet());
                    Log.d("location data", "Current data: " + snapshot.getData());
                } else {
                    Log.d("location data", "Current data: null");
                }
            }
        });

        //  fetches Units from Firestore
        ArrayList<String> unitOptions = new ArrayList<>();
        CollectionReference unitColRef = databaseInstance.collection("Options");
        final DocumentReference unitDocRef = unitColRef.document("Units");

        unitDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    unitOptions.clear();
                    unitOptions.add(addOption);
                    unitOptions.addAll(0, Objects.requireNonNull(snapshot.getData()).keySet());
                    Log.d("unit data", "Current data: " + snapshot.getData());
                } else {
                    Log.d("unit data", "Current data: null");
                }
            }
        });



        //  listener for each Ingredient in list
        ArrayList<String> what;
        ingredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ViewIngredientActivity.class);
                clickedIngredient = (StoredIngredient) ingredientAdapter.getItem(i);
                intent.putExtra("INGREDIENT", clickedIngredient);
                intent.putExtra("CATEGORIES", catOptions);
                intent.putExtra("LOCATIONS", locOptions);
                intent.putExtra("UNITS", unitOptions);
                activityResultLauncher.launch(intent);
            }
        });

        //  handles returning from editing, adding, or deleting and Ingredient
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null) {
                    Intent intent = result.getData();

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

                    } else if (result.getResultCode() == 401) {
                        StoredIngredient edittedIngredient = new StoredIngredient(description, amount, unit, category, date, location);
                        db.editIngredient(clickedIngredient,edittedIngredient);


                    } else if (result.getResultCode() == 402) {
                        db.deleteStoredIngredient(clickedIngredient);
                        ingredients.remove(clickedIngredient);
                    }
                    ingredientAdapter.notifyDataSetChanged();
                }
            }
        });

        //  add new Ingredient button
        FloatingActionButton addIngredientButton = findViewById(R.id.add_item_button);
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add ingredient activity
                Intent intent = new Intent(getApplicationContext(), AddIngredientActivity.class);
                intent.putExtra("CATEGORIES", catOptions);
                intent.putExtra("LOCATIONS", locOptions);
                intent.putExtra("UNITS", unitOptions);
                activityResultLauncher.launch(intent);
            }
        });

        //  sorting Ingredient list
        sortButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i) == "Category") {
                    ingredients.sort(Comparator.comparing(Ingredient::getCategory));
                } else if (adapterView.getItemAtPosition(i) == "Description") {
                    ingredients.sort(Comparator.comparing(Ingredient::getDescription));
                } else if (adapterView.getItemAtPosition(i) == "Best Before Date") {
                    ingredients.sort(Comparator.comparing(StoredIngredient::getDate));
                }
                ingredientAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
