package com.example.menuscript;

import static android.content.ContentValues.TAG;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * class for adding a recipe to meal plan.
 * need to pass initial list of meal plan recipe keys from previous activity
 * @author Wanlin
 */
public class AddRecipeMealPlanActivity extends AppCompatActivity {

    private TextView recipeServingsTextView;
    private TextView selectItemTextView;
    private Button addNewRecipeButton;
    private EditText recipeServingsEditText;
    private Button addToMealPlanButton;
    private Spinner recipesSpinner;

    private FirebaseFirestore databaseInstance;
    private CollectionReference recipesCollectionReference;
    private CollectionReference mealPlanRecipesCollectionReference;
    private ArrayList recipesList;
    private ArrayList spinnerArray;
    //meal plan ingredients list  of ingredient keys so ingredients already in the meal plan are not shown in the spinner
    private ArrayList mealPlanRecipesList;
    private ArrayAdapter<String> spinnerAdapter;


    private String recipeKeytoAdd;
    private String recipeTitletoAdd;
    private String recipeServings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_plan_add);

        mealPlanRecipesList = new ArrayList<>();
        recipesList = new ArrayList<>();
        spinnerArray = new ArrayList<>();
        recipeKeytoAdd = null;
        recipeTitletoAdd = null;
        recipeServings = null;

        recipeServingsTextView = findViewById(R.id.selectQuantityTextView);
        selectItemTextView = findViewById(R.id.selectItemTextView);
        addNewRecipeButton = findViewById(R.id.NewItemButton);
        recipeServingsEditText = findViewById(R.id.itemAmountEditText);
        addToMealPlanButton = findViewById(R.id.addButton);
        recipesSpinner = findViewById(R.id.menuItemSpinner);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,spinnerArray);
        recipesSpinner.setAdapter(spinnerAdapter);




        databaseInstance = FirebaseFirestore.getInstance();
        recipesCollectionReference = databaseInstance.collection("Recipes");
        mealPlanRecipesCollectionReference = databaseInstance.collection("MealPlanRecipes");



        //need to pass initial list of meal plan keys
        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getString("RecipeKeys")!=null){
            mealPlanRecipesList.clear();
            mealPlanRecipesList.addAll(bundle.getStringArrayList("RecipeKeys"));
        }else{
            Log.d(TAG,"NO MEAL PLAN Recipe KEY PASSED");
        }

        //get meal plan ingredients
        mealPlanRecipesCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentsSnapshots, @Nullable FirebaseFirestoreException error) {

                mealPlanRecipesList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentsSnapshots) {
                    Log.d(TAG,String.valueOf(doc.getData().get("title")));
                    String recipeKey = doc.getId();
                    mealPlanRecipesList.add(recipeKey);
                }
            }
        });

        recipesCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentsSnapshots, @Nullable FirebaseFirestoreException error) {
                spinnerArray.clear();
                recipesList.clear();

                for (QueryDocumentSnapshot doc : queryDocumentsSnapshots) {
                    Log.d(TAG,String.valueOf(doc.getData().get("title")));

                    String recipeKey = doc.getId();
                    String recipeDescription = (String) doc.getData().get("title");
                    if (!mealPlanRecipesList.contains(recipeKey)){
                        recipesList.add(doc.getId());
                        spinnerArray.add(recipeDescription);
                    }

                }
                spinnerAdapter.notifyDataSetChanged();
            }
        });

        recipesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                recipeTitletoAdd = (String) spinnerArray.get(i);
                recipeKeytoAdd = (String) recipesList.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                recipeTitletoAdd = null;
                recipeKeytoAdd = null;
            }

        });

        recipeServingsTextView.setText("Select Number of Servings");
        selectItemTextView.setText("Select Recipe");
        addToMealPlanButton.setText("Add Recipe to Meal Plan");
        addNewRecipeButton.setText("Add New Recipe");

        addToMealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeServings = recipeServingsEditText.getText().toString();
                HashMap<String, String> data = new HashMap<>();

                if (recipeTitletoAdd.length() > 0 && recipeKeytoAdd.length() > 0 && recipeServings.length() > 0) {
                    data.put("servings", recipeServings);
                    data.put("title", recipeTitletoAdd);

                    mealPlanRecipesCollectionReference
                            .document(recipeKeytoAdd)
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
                                    recipeServingsEditText.setText("");
                                }
                            });


                } else {
                    CharSequence text = "Missing input.";
                    Toast.makeText(AddRecipeMealPlanActivity.this, text, Toast.LENGTH_SHORT).show();
                }


            }
        });

        addNewRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addRecipeIntent = new Intent(AddRecipeMealPlanActivity.this, AddRecipeActivity.class);
                AddRecipeMealPlanActivity.this.startActivity(addRecipeIntent);
            }
        });


    }
}
