package com.example.menuscript;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * This class controls main meal plan that displays current meal plan ingredients and recipes:
 *
 * previous activity needs to pass current duration of meal plan "days" from database, else default 0
 * @author Wanlin,
 */
public class MealPlanActivity extends AppCompatActivity {

    private Button addIngredientButton;
    private Button addRecipeButton;
    private ListView ingredientListView;
    private ListView recipesListView;
    private EditText daysEditText;
    private Button clearMealPlanButton;
    private FirebaseFirestore databaseInstance;
    private CollectionReference daysCollectionReference;
    private CollectionReference mealPlanRecipesCollectionReference;
    private CollectionReference mealPlanIngredientCollectionReference;
    private Button saveDaysButton;
    private String days;
    private ArrayList<String> mealPlanIngredientKeys;
    private ArrayList<String> mealPlanRecipeKeys;
    private ArrayList<String> mealPlanIngredientsList;
    private ArrayList<String> mealPlanRecipesList;
    private ArrayAdapter<String> ingredientsAdapter;
    private ArrayAdapter<String> recipesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_plan_main);
        addIngredientButton = findViewById(R.id.add_item_button);
        addRecipeButton = findViewById(R.id.addRecipesButton);
        ingredientListView = findViewById(R.id.ingredientListView);
        recipesListView = findViewById(R.id.recipeListView);
        daysEditText = findViewById(R.id.planDurationEditText);
        clearMealPlanButton = findViewById(R.id.clearMealPlanButton);
        saveDaysButton = findViewById(R.id.saveDaysButton);
        mealPlanRecipeKeys = new ArrayList<>();
        mealPlanIngredientKeys = new ArrayList<>();
        mealPlanIngredientsList = new ArrayList<>();
        mealPlanRecipesList = new ArrayList<>();
        ingredientsAdapter = new ArrayAdapter<>(this, R.layout.content, mealPlanIngredientsList);
        recipesAdapter = new ArrayAdapter<>(this, R.layout.content, mealPlanRecipesList);
        ingredientListView.setAdapter(ingredientsAdapter);
        recipesListView.setAdapter(recipesAdapter);

        databaseInstance = FirebaseFirestore.getInstance();
        daysCollectionReference = databaseInstance.collection("MealPlanDays");
        mealPlanRecipesCollectionReference = databaseInstance.collection("MealPlanRecipes");
        mealPlanIngredientCollectionReference = databaseInstance.collection("MealPlanIngredients");


        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("Days")!=null){
            days = (bundle.getString("Days"));
        }else{
            days = "0";
        }


        daysEditText.setText(days);
        saveDaysButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String daysInput = daysEditText.getText().toString();

                if (daysInput == days){
                    CharSequence text = "Duration is already set to that number.";
                    Toast.makeText(MealPlanActivity.this, text, Toast.LENGTH_SHORT).show();
                }
                else if (daysInput.length() > 0) {

                    daysCollectionReference
                            .document("Days")
                            .update("days" , daysInput)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "Data added successfully");
                                    days = daysInput;
                                    daysEditText.setText(days);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Data could not be added" + e.toString());
                                    Toast.makeText(MealPlanActivity.this, "failed to change database", Toast.LENGTH_SHORT).show();
                                }
                            });


                } else {
                    CharSequence text = "Invalid input";
                    Toast.makeText(MealPlanActivity.this, text, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int indextoDelete = i;
                AlertDialog.Builder builder = new AlertDialog.Builder(MealPlanActivity.this);
                builder
                        .setMessage("Delete ingredient from meal plan?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mealPlanIngredientCollectionReference.document(mealPlanIngredientKeys.get(indextoDelete)).delete();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        recipesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int indextoDelete = i;
                AlertDialog.Builder builder = new AlertDialog.Builder(MealPlanActivity.this);
                builder
                        .setMessage("Delete recipe from meal plan?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mealPlanRecipesCollectionReference.document(mealPlanRecipeKeys.get(indextoDelete)).delete();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        addRecipeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("RecipeKeys", mealPlanRecipeKeys);
                Intent intent = new Intent(MealPlanActivity.this, AddRecipeMealPlanActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);


            }

        });

        addIngredientButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("IngredientKeys", mealPlanIngredientKeys);
                Intent intent = new Intent(MealPlanActivity.this, AddIngredientMealPlanActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        });


        mealPlanRecipesCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentsSnapshots, @Nullable FirebaseFirestoreException error) {
                mealPlanRecipesList.clear();
                mealPlanRecipeKeys.clear();
                for (QueryDocumentSnapshot doc : queryDocumentsSnapshots) {
                    Log.d(TAG,String.valueOf(doc.getData().get("title")));
                    String recipeTitle = (String) doc.getData().get("title");
                    String recipeKey = doc.getId();
                    mealPlanRecipesList.add(recipeTitle);
                    mealPlanRecipeKeys.add(recipeKey);
                }
                recipesAdapter.notifyDataSetChanged();
            }
        });

        mealPlanIngredientCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentsSnapshots, @Nullable FirebaseFirestoreException error) {
                mealPlanIngredientsList.clear();
                mealPlanIngredientKeys.clear();
                for (QueryDocumentSnapshot doc : queryDocumentsSnapshots) {
                    Log.d(TAG,String.valueOf(doc.getData().get("description")));
                    String ingredientDescription = (String) doc.getData().get("description");
                    String ingredientKey = doc.getId();
                    mealPlanIngredientsList.add(ingredientDescription);
                    mealPlanIngredientKeys.add(ingredientKey);
                }
                ingredientsAdapter.notifyDataSetChanged();
            }
        });



    }
}
