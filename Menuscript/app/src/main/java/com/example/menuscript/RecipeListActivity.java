package com.example.menuscript;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class controls the view recipes list activity:
 * Users can click on a recipe to view its details. The sort button will allow a user to sort through their recipes by a desired sorter.
 * Clicking the button on the bottom right will bring a user to the AddRecipeActivity
 * recipeList {@link ListView}
 * recipeAdapter {@link ArrayAdapter}
 * dataList {@link ArrayList<Recipe>}
 * header {@link TextView}
 * selectedRecipe {@link Recipe}
 *
 * @author Wanlin, Dylan
 */

public class RecipeListActivity extends AppCompatActivity {
    ListView recipeList;
    ArrayAdapter<Recipe> recipeAdapter;
    ArrayList<Recipe> dataList;
    TextView header;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    Recipe selectedRecipe;
    private FirebaseFirestore databaseInstance;
    private CollectionReference collectionReference;
    DatabaseManager db = new DatabaseManager(this);


//_____________________TESTING_______________________
    ArrayList<Ingredient> ingredients;
//---------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        header = findViewById(R.id.header);
        header.setText(R.string.recipes);

        recipeList = findViewById(R.id.item_list);
        dataList = new ArrayList<Recipe>();
        recipeAdapter = new RecipeListAdapter(this, dataList);
        recipeList.setAdapter(recipeAdapter);
        databaseInstance = FirebaseFirestore.getInstance();
        collectionReference = databaseInstance.collection("Recipes");



        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                dataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String title = (String) doc.getData().get("title");
                    float servings = Float.parseFloat(String.valueOf(doc.getData().get("servings")));
                    String comments = (String) doc.getData().get("comments");
                    String category = (String) doc.getData().get("category");
                    int time = Math.toIntExact((long) doc.getData().get("time"));
                    byte[] image = android.util.Base64.decode((String) doc.getData().get("image"), Base64.DEFAULT);

                    ArrayList<HashMap<String,String>> ingredientsList = new ArrayList<>();
                    ingredientsList =(ArrayList<HashMap<String, String>>)doc.get("ingredients");
                    ArrayList<Ingredient> ingredients = new ArrayList<>();
                    for(HashMap<String,String> ingredient: ingredientsList){
                        ingredients.add(new Ingredient(
                                String.valueOf(ingredient.get("description")),
                                Float.parseFloat(String.valueOf(ingredient.get("amount"))),
                                String.valueOf(ingredient.get("unit")),
                                String.valueOf(ingredient.get("category")) ));
                    }
                    dataList.add(new Recipe(title, time, servings, category, comments, image, ingredients));
                }
                recipeAdapter.notifyDataSetChanged();
            }
        });

        //  fetches Categories from Firestore
        ArrayList<String> catOptions = new ArrayList<>();
        CollectionReference catColRef = databaseInstance.collection("Options");
        final DocumentReference catDocRef = catColRef.document("Recipe Categories");
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
                    catOptions.add("Add new item");
                    catOptions.addAll(0, Objects.requireNonNull(snapshot.getData()).keySet());
                    Log.d("category data", "Current data: " + snapshot.getData());
                } else {
                    Log.d("category data", "Current data: null");
                }
            }
        });

        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RecipeListActivity.this, ViewRecipeActivity.class);
                selectedRecipe = recipeAdapter.getItem(i);
                intent.putExtra("NAME", selectedRecipe.getTitle());
                intent.putExtra("TIME", selectedRecipe.getTime());
                intent.putExtra("CATEGORY", selectedRecipe.getCategory());
                intent.putExtra("SERVINGS", selectedRecipe.getServings());
                intent.putExtra("COMMENTS", selectedRecipe.getComments());
                intent.putExtra("IMAGE", selectedRecipe.getImage());
                intent.putExtra("CATEGORY LIST", catOptions);
                Bundle args = new Bundle();
                args.putSerializable("INGREDIENTS", selectedRecipe.getIngredients());
                intent.putExtra("INGREDIENTS_BUNDLE", args);

                //startActivity(intent);
                activityResultLauncher.launch(intent);
            }
        });


        Spinner sortButton = findViewById(R.id.sort_button);
        String[] sortOptions = new String[]{ "Title", "Preparation time", "Number of servings", "Category"};
        CustomSortAdapter<String> sortAdapter = new CustomSortAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sortOptions);
        sortButton.setAdapter(sortAdapter);

        sortButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i) == "Category") {
                    dataList.sort(Comparator.comparing(Recipe::getCategory));
                } else if (adapterView.getItemAtPosition(i) == "Title") {
                    dataList.sort(Comparator.comparing(Recipe::getTitle));
                }
                else if (adapterView.getItemAtPosition(i) == "Preparation time") {
                    dataList.sort(Comparator.comparing(Recipe::getTime));
                }
                else if (adapterView.getItemAtPosition(i) == "Number of servings") {
                    dataList.sort(Comparator.comparing(Recipe::getServings));
                }
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getData() != null){
                    Intent intent = result.getData();
                    String title = intent.getStringExtra("title");
                    int time = intent.getIntExtra("time",0);
                    float servings = intent.getFloatExtra("servings",0.0f);
                    String category = intent.getStringExtra("category");
                    String comments = intent.getStringExtra("comments");
                    byte[] image = intent.getByteArrayExtra("image"); //CORRESPONDS TO LINES IN ADDRECIPEACTIVITY
                    Bundle args = intent.getBundleExtra("BUNDLE");

                    if(result.getResultCode() == 420) {
                        //addRecipe
                        Recipe newRecipe = new Recipe(title, time, servings, category, comments, image, (ArrayList<Ingredient>) args.getSerializable("INGREDIENTSLIST"));
                        db.addRecipe(newRecipe);
                        dataList.add(newRecipe);

                    } else if (result.getResultCode() == 421){
                        //edit/view Recipe
                        Recipe edittedRecipe = new Recipe(title, time, servings, category, comments, image, (ArrayList<Ingredient>) args.getSerializable("INGREDIENTSLIST"));
                        db.editRecipe(selectedRecipe,edittedRecipe);


                    } else if (result.getResultCode() == 422){
                        db.deleteRecipe(selectedRecipe);
                        dataList.remove(selectedRecipe);
                    }
                    recipeAdapter.notifyDataSetChanged();

                }
            }
        });

        FloatingActionButton addRecipeButton = findViewById(R.id.add_item_button);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add recipe activity
                Intent intent = new Intent(getApplicationContext(),AddRecipeActivity.class);
                intent.putExtra("ingredients",ingredients); //NOTE: USING TEMPORARY ingredients ARRAYLIST -- PLEASE NOTE FOR FULL IMPLEMENTATION
                intent.putExtra("CATEGORIES", catOptions);
                activityResultLauncher.launch(intent);
            }
        });
    }
}
