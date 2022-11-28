package com.example.menuscript;

import static android.content.ContentValues.TAG;
import static android.provider.MediaStore.MediaColumns.DOCUMENT_ID;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

/**
 * This class displays a list of ingredients that are currently in the meal plan.
 * Clicking on an ingredient shows some of the details relevant to shopping.
 * User can check off multiple ingredients and click the confirm button on the bottom
 * <p>
 * Requires being passed hashmap of ingredients (key, amount) needed from previous activity (Main)
 *
 * @author Josh, Wanlin
 */
public class ShopListActivity extends AppCompatActivity {

    private Button addIngredientsButton;
    private ArrayList<Ingredient> ingredientToBeAddedList;
    ShopListAdapter shoppingAdapter;
    ArrayList<Ingredient> ingredientList;
    HashMap<String, StoredIngredient> storedIngredientsList;
    HashMap<String, Float> mealPlanIngredients; // {hashmap of ingredient key, amount needed}
    HashMap<String, Float> mealPlanRecipes; // {hashmap of recipe key, servings needed}

    ListView shoppingList;
    Ingredient selectedIngredient;
    StoredIngredient storedIngredient;
    Button confirmButton;
    DatabaseManager db = new DatabaseManager(this);
    private ActivityResultLauncher<Intent> activityResultLauncher;

    private FirebaseFirestore databaseInstance;
    private CollectionReference recipesCollection;
    private Spinner sortSpinner;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoplist_main);

        ingredientList = new ArrayList<Ingredient>();

        Bundle args = getIntent().getBundleExtra("MEALPLAN_ITEMS");
        if (args.getSerializable("MEALPLAN_INGREDIENTS") != null) {
            mealPlanIngredients = (HashMap<String, Float>) args.getSerializable("MEALPLAN_INGREDIENTS");
        } else {
            mealPlanIngredients = new HashMap<>();
        }

        if (args.getSerializable("MEALPLAN_RECIPES") != null) {
            mealPlanRecipes = (HashMap<String, Float>) args.getSerializable("MEALPLAN_RECIPES");
        } else {
            mealPlanRecipes = new HashMap<>();
        }

        if (args.getSerializable("STORED_INGREDIENTS") != null) {
            storedIngredientsList = (HashMap<String, StoredIngredient>) args.getSerializable("STORED_INGREDIENTS");
        } else {
            storedIngredientsList = new HashMap<>();
        }

        shoppingList = findViewById(R.id.shopListMainIngredients);
        sortSpinner = findViewById(R.id.shopListMainSpinner);
        confirmButton = findViewById(R.id.shopListButton);
        shoppingAdapter = new ShopListAdapter(this, ingredientList);
        shoppingList.setAdapter(shoppingAdapter);
        databaseInstance = FirebaseFirestore.getInstance();
        recipesCollection = databaseInstance.collection("Recipes");


        for (String key : mealPlanIngredients.keySet()) {
            if (storedIngredientsList.keySet().contains(key)) {
                Ingredient ingredientToAdd = storedIngredientsList.get(key);
                Float amountNeeded = mealPlanIngredients.get(key) - ingredientToAdd.getAmount();
                //ingredientToAdd.setAmount(amountNeeded);
                Ingredient newIngredient = new Ingredient(ingredientToAdd.getDescription(), 0, ingredientToAdd.getUnit(), ingredientToAdd.getCategory());
                newIngredient.setAmount(amountNeeded);
                ingredientList.add(newIngredient);
                Log.d("CHECKLIST", "oldstoredIngredient START" + String.valueOf(storedIngredientsList.get(key).amount));
            }
        }


        recipesCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.d("SHOPPING LIST", "ONEVENT RECIPES");
                for (QueryDocumentSnapshot doc : value) {
                    if (mealPlanRecipes.keySet().contains(doc.getId())) {
                        Float servings = mealPlanRecipes.get(doc.getId());
                        Float recipeServings = Float.parseFloat((String) doc.getData().get("servings"));

                        ArrayList<HashMap<String, String>> recipeIngredientsList = new ArrayList<>();
                        recipeIngredientsList = (ArrayList<HashMap<String, String>>) doc.get("ingredients");
                        ArrayList<Ingredient> ingredients = new ArrayList<>();

                        for (HashMap<String, String> ingredient : recipeIngredientsList) {
                            Boolean alreadyInIngredientList = false;
                            String description = String.valueOf(ingredient.get("description"));
                            String unit = String.valueOf(ingredient.get("unit"));
                            String category = String.valueOf(ingredient.get("category"));

                            for (Ingredient ingredient1 : ingredientList) { //checking if already in IngredientList
                                if ((ingredient1.getDescription().equals(description)) && (ingredient1.getCategory().equals(category)) && (ingredient1.getUnit().equals(unit))) {
                                    float neededAmount = ingredient1.getAmount() + (servings * (Float.parseFloat(String.valueOf(ingredient.get("amount"))) / recipeServings));
                                    ingredient1.setAmount(neededAmount);
                                    alreadyInIngredientList = true;
                                }
                            }

                            if (!alreadyInIngredientList) {
                                //check if in storedIngredientsList
                                boolean inStoredIngredientsList = false;
                                for (String key : storedIngredientsList.keySet()) {
                                    Ingredient storedIngredient1 = storedIngredientsList.get(key);
                                    if ((storedIngredient1.getDescription().equals(description)) && (storedIngredient1.getCategory().equals(category)) && (storedIngredient1.getUnit().equals(unit))) {
                                        Float amountNeeded = ((Float.parseFloat(String.valueOf(ingredient.get("amount"))) / recipeServings) * servings) - storedIngredient1.getAmount();
                                        //storedIngredient1.setAmount(amountNeeded);
                                        ingredientList.add(new Ingredient(storedIngredient1.getDescription(), amountNeeded, storedIngredient1.getUnit(), storedIngredient1.getCategory()));
                                        inStoredIngredientsList = true;
                                    }

                                }

                                if (!inStoredIngredientsList) {
                                    ingredientList.add(new Ingredient(
                                            description,
                                            ((Float.parseFloat(String.valueOf(ingredient.get("amount"))) / recipeServings) * servings),
                                            unit,
                                            category));
                                    alreadyInIngredientList = false;
                                }

                            }
                        }
                    }
                }

                //check ingredients list for negative amountsNeeded

                ingredientList.removeIf(ingredient -> ingredient.getAmount() <= 0);
                shoppingAdapter.notifyDataSetChanged();
            }
        });

        /**
         * Displays information about ingredient clicked
         */
        shoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ShopListActivity.this, ViewShopListIngredientActivity.class);
                selectedIngredient = shoppingAdapter.getItem(i);
                intent.putExtra("NAME", selectedIngredient.getDescription());
                intent.putExtra("AMOUNT", selectedIngredient.getAmount());
                intent.putExtra("UNIT", selectedIngredient.getUnit());
                intent.putExtra("CATEGORY", selectedIngredient.getCategory());

                startActivity(intent);
            }
        });


        //sort button
        String sortOptions[] = {"Sort by Category", "Sort by Description"};
        CustomSortAdapter<String> sortAdapter = new CustomSortAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sortOptions);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Sort by Category")) {
                    ingredientList.sort(Comparator.comparing(Ingredient::getCategory));
                } else if (adapterView.getItemAtPosition(i).equals("Sort by Description")) {
                    ingredientList.sort(Comparator.comparing(Ingredient::getDescription));
                }
                shoppingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                } else {
                    Log.d("unit data", "Current data: null");
                }
            }
        });

        /**
         * Passes information to IngredientViewActivity to create or edit
         */
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Ingredient ingredient : shoppingAdapter.getIngredients()) {
                    String description = ingredient.getDescription();
                    String unit = ingredient.getUnit();
                    String category = ingredient.getCategory();
                    Float amount = ingredient.getAmount();
                    String date = "";
                    String location = "";
                    String storedIngredientKey = "";

                    //if already a stored ingredient
                    boolean inStoredIngredientsList = false;

                    for (String key : storedIngredientsList.keySet()) {
                        StoredIngredient oldStoredIngredient = storedIngredientsList.get(key);
                        if ((oldStoredIngredient.getDescription().equals(description)) && (oldStoredIngredient.getCategory().equals(category)) && (oldStoredIngredient.getUnit().equals(unit))) {
                            Log.d("CHECKLIST", "description" + description);
                            Log.d("CHECKLIST", "oldStoredIngredient amount1" + String.valueOf(amount));
                            amount = amount + oldStoredIngredient.getAmount();
                            Log.d("CHECKLIST", "oldStoredIngredient old amount" + String.valueOf(oldStoredIngredient.getAmount()));
                            location = oldStoredIngredient.getLocation();
                            date = oldStoredIngredient.getDate();
                            storedIngredientKey = key;
                            inStoredIngredientsList = true;
                            storedIngredient = oldStoredIngredient;
                            Log.d("CHECKLIST", "oldStoredIngredient amount2" + String.valueOf(amount));
                        }
                    }

                    StoredIngredient newIngredient = new StoredIngredient(description, amount, unit, category, date, location);
                    Log.d("CHECKLIST", storedIngredientKey);
                    if (!inStoredIngredientsList) {
                        db.addStoredIngredient(newIngredient);
                    }

                    Intent intent = new Intent(getApplicationContext(), ViewIngredientActivity.class);
                    intent.putExtra("INGREDIENT", newIngredient);
                    intent.putExtra("CATEGORIES", catOptions);
                    intent.putExtra("LOCATIONS", locOptions);
                    intent.putExtra("UNITS", unitOptions);
                    activityResultLauncher.launch(intent);
                }


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

                    if (result.getResultCode() == 401) {
                        StoredIngredient edittedIngredient = new StoredIngredient(description, amount, unit, category, date, location);
                        db.editIngredient(storedIngredient, edittedIngredient);

                        //-------------------------------------ALLOWS ONLY THE EDIT OF ONE ITEM = LESS ERRORS WITH CURRENT VERSION --------------------
                        finish();
                        // ----------------------------------------NOT INTENDED FUNCTION


                    } else if (result.getResultCode() == 402) {
                        db.deleteStoredIngredient(storedIngredient);
                    }
                }
            }
        });

    }
}
