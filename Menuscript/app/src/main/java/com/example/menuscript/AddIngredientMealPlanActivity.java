package com.example.menuscript;

import static android.content.ContentValues.TAG;
import static android.view.accessibility.AccessibilityEvent.INVALID_POSITION;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * class for adding a ingredient to meal plan.
 * need to pass initial list of meal plan  ingredient keys from previous activity
 * @author Wanlin
 */

public class AddIngredientMealPlanActivity extends AppCompatActivity {

    private TextView selectItemTextView;
    private TextView selectAmountTextView;
    private Button addNewIngredientButton;
    private EditText ingredientAmountEditText;
    private Button addToMealPlanButton;
    private Spinner ingredientsSpinner;
    private FirebaseFirestore databaseInstance;
    private CollectionReference ingredientCollectionReference;
    private CollectionReference mealPlanIngredientCollectionReference;
    private ArrayList ingredientsList;
    private ArrayList ingredientUnitList;
    private ArrayList spinnerArray;
    //meal plan ingredients list  of ingredient keys so ingredients already in the meal plan are not shown in the spinner
    private ArrayList mealPlanIngredientsList;
    private ArrayAdapter<String> spinnerAdapter;


    private String ingredientKeytoAdd;
    private String ingredientDescriptiontoAdd;
    private String ingredientAmount;
    private String ingredientUnittoAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_plan_add);

        mealPlanIngredientsList = new ArrayList<>();
        ingredientsList = new ArrayList<>();
        spinnerArray = new ArrayList<>();
        ingredientUnitList = new ArrayList<>();
        ingredientKeytoAdd = null;
        ingredientDescriptiontoAdd = null;
        ingredientAmount = null;
        selectItemTextView = findViewById(R.id.selectItemTextView);
        selectAmountTextView = findViewById(R.id.selectQuantityTextView);
        addNewIngredientButton = findViewById(R.id.NewItemButton);
        ingredientAmountEditText = findViewById(R.id.itemAmountEditText);
        addToMealPlanButton = findViewById(R.id.addButton);
        ingredientsSpinner = findViewById(R.id.menuItemSpinner);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,spinnerArray);
        ingredientsSpinner.setAdapter(spinnerAdapter);

        databaseInstance = FirebaseFirestore.getInstance();
        ingredientCollectionReference = databaseInstance.collection("StoredIngredients");
        mealPlanIngredientCollectionReference = databaseInstance.collection("MealPlanIngredients");


        //need to pass initial list of meal plan keys
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getStringArrayList("IngredientKeys")!=null){
            mealPlanIngredientsList.clear();
            mealPlanIngredientsList = (bundle.getStringArrayList("IngredientKeys"));

        }else{
            Log.d("ADDINGREDIENT","NO MEAL PLAN INGREDIENT KEY PASSED");
        }

        //get meal plan ingredients
        mealPlanIngredientCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentsSnapshots, @Nullable FirebaseFirestoreException error) {

                mealPlanIngredientsList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentsSnapshots) {
                    Log.d(TAG,String.valueOf(doc.getData().get("description")));
                    String ingredientKey = doc.getId();
                    mealPlanIngredientsList.add(ingredientKey);
                }
            }
        });

        ingredientCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentsSnapshots, @Nullable FirebaseFirestoreException error) {
                spinnerArray.clear();
                ingredientsList.clear();
                ingredientUnitList.clear();

                for (QueryDocumentSnapshot doc : queryDocumentsSnapshots) {
                    Log.d(TAG,String.valueOf(doc.getData().get("description")));

                    String ingredientKey = doc.getId();
                    String ingredientDescription = (String) doc.getData().get("description");
                    String ingredientUnit = (String) doc.getData().get("unit");
                    if (!(mealPlanIngredientsList.contains( ingredientKey))){
                        ingredientsList.add(doc.getId());
                        spinnerArray.add(ingredientDescription);
                        ingredientUnitList.add(ingredientUnit);
                    }

                }
                spinnerAdapter.notifyDataSetChanged();
            }
        });

        ingredientsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ingredientDescriptiontoAdd = (String) spinnerArray.get(i);
                ingredientKeytoAdd = (String) ingredientsList.get(i);
                ingredientUnittoAdd = (String) ingredientUnitList.get(i);
                String selectAmount = "Select amount (" + ingredientUnittoAdd + ") :";
                selectAmountTextView.setText(selectAmount);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ingredientDescriptiontoAdd = null;
            }

        });

        String selectItem = "Select Ingredient";
        selectItemTextView.setText(selectItem);

        String addToPlan = "Add Ingredient to Meal Plan";
        addToMealPlanButton.setText(addToPlan);

        String addIngredient = "Add New Ingredient";
        addNewIngredientButton.setText(addIngredient);

        addToMealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientAmount = ingredientAmountEditText.getText().toString();
                int index = ingredientsSpinner.getSelectedItemPosition();
                if (index != INVALID_POSITION){
                    ingredientDescriptiontoAdd = (String) spinnerArray.get(index);
                    ingredientKeytoAdd = (String) ingredientsList.get(index);
                    ingredientUnittoAdd = (String) ingredientUnitList.get(index);
                }
                else{
                    ingredientDescriptiontoAdd = null;
                    ingredientKeytoAdd = null;
                    ingredientUnittoAdd = null;
                }
                HashMap<String, String> data = new HashMap<>();

                if (ingredientDescriptiontoAdd != null && ingredientKeytoAdd.length() > 0 && ingredientAmount.length() > 0) {
                    data.put("amount", ingredientAmount);
                    data.put("description", ingredientDescriptiontoAdd);
                    data.put("unit", ingredientUnittoAdd);

                    mealPlanIngredientCollectionReference
                            .document(ingredientKeytoAdd)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "Data added successfully");
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Data could not be added" + e.toString());
                                    ingredientAmountEditText.setText("");
                                }
                            });


                } else {
                    CharSequence text = "Missing input.";
                    Toast.makeText(AddIngredientMealPlanActivity.this, text, Toast.LENGTH_SHORT).show();
                }


            }
        });


        //  fetches Categories from Firestore
        final String addOption = "Add new item";
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

        addNewIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIngredientIntent = new Intent(AddIngredientMealPlanActivity.this, AddIngredientActivity.class);
                addIngredientIntent.putExtra("CATEGORIES", catOptions);
                addIngredientIntent.putExtra("LOCATIONS", locOptions);
                addIngredientIntent.putExtra("UNITS", unitOptions);
                AddIngredientMealPlanActivity.this.startActivity(addIngredientIntent);
            }
        });




    }
}
