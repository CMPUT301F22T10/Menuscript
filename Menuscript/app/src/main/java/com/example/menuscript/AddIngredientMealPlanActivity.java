package com.example.menuscript;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.HashMap;

public class AddIngredientMealPlanActivity extends AppCompatActivity {

    private TextView selectItemTextView;
    private Button addNewIngredientButton;
    private EditText ingredientAmountEditText;
    private Button addToMealPlanButton;
    private Spinner ingredientsSpinner;
    private FirebaseFirestore databaseInstance;
    private CollectionReference ingredientCollectionReference;
    private CollectionReference mealPlanIngredientCollectionReference;
    private HashMap<String, String> ingredientsList;
    private ArrayList spinnerArray;
    //meal plan ingredients list  of ingredient keys so ingredients already in the meal plan are not shown in the spinner
    private ArrayList mealPlanIngredientsList;

    private ArrayAdapter<String> spinnerAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_plan_add);

        selectItemTextView = findViewById(R.id.selectItemTextView);
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
        if (bundle.getString("IngredientKeys")!=null){
            mealPlanIngredientsList = bundle.getStringArrayList("IngredientKeys");
        }else{
            Log.d(TAG,"NO MEAL PLAN INGREDIENT KEY PASSED");
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

                for (QueryDocumentSnapshot doc : queryDocumentsSnapshots) {
                    Log.d(TAG,String.valueOf(doc.getData().get("description")));

                    String ingredientKey = doc.getId();
                    String ingredientDescription = (String) doc.getData().get("description");
                    if (!mealPlanIngredientsList.contains(ingredientKey)){
                        ingredientsList.put(doc.getId(), ingredientDescription);
                        spinnerArray.add(ingredientDescription);
                    }

                }
            }
        });

        ingredientsSpinner.setOnItemSelectedListener();






    }
}
