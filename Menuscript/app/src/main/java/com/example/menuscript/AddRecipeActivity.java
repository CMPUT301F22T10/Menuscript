package com.example.menuscript;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class controls the addition of a new recipe.
 * Users can input desired attributes (within restrictions) and click the top-left button to add the recipe.
 * Currently, adding of specific ingredients is not implemented.
 * recipeName {@link EditText}
 * recipePrepTime {@link EditText}
 * recipeServings {@link EditText}
 * recipeCategory {@link EditText}
 * recipeComments {@link EditText}
 * recipeImage {@link ImageButton}
 * ingredientAdapter {@link CustomIngredientList}
 * ingredientList {@link ListView}
 *
 * @author Dylan Clarke
 */

public class AddRecipeActivity extends AppCompatActivity implements AddOptionFragment.OnFragmentInteractionListener {
    EditText recipeTitle;
    EditText recipeTime;
    EditText recipeServings;
    Spinner recipeCategory;
    EditText recipeComments;
    ListView recipeIngredientList;
    ListView recipeAddedIngredientList;
    ImageButton recipeImage;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ArrayAdapter<Ingredient> ingredientAdapter;
    ArrayAdapter<Ingredient> addedIngredientAdapter;
    ArrayList<Ingredient> ingredientList;
    ArrayList<Ingredient> addedIngredientList;

    ArrayAdapter<String> catAdapter;
    ArrayList<String> recipeCatOptions;

    private FirebaseFirestore databaseInstance;
    private CollectionReference collectionReference;
    DatabaseManager db = new DatabaseManager(this);

    StoredIngredient clickedStoredIngredient;
    Ingredient clickedIngredient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_add);

        recipeTitle = findViewById(R.id.recipeAddTitle);
        recipeTime = findViewById(R.id.recipeAddTime);
        recipeServings = findViewById(R.id.recipeAddServings);
        recipeCategory = findViewById(R.id.recipeAddCategory);
        recipeComments = findViewById(R.id.recipeAddComments);
        recipeIngredientList = findViewById(R.id.recipeAddIngredientsList);
        recipeAddedIngredientList = findViewById(R.id.recipeAddIngredientListAdded);

        databaseInstance = FirebaseFirestore.getInstance();
        collectionReference = databaseInstance.collection("StoredIngredients");
        ingredientList = new ArrayList<>();
        ingredientAdapter = new RecipeIngredientListAdapter(this, ingredientList);
        recipeIngredientList.setAdapter(ingredientAdapter);

        addedIngredientList = new ArrayList<>();
        addedIngredientAdapter = new RecipeIngredientListAdapter(this, addedIngredientList);
        recipeAddedIngredientList.setAdapter(addedIngredientAdapter);

        recipeCatOptions = (ArrayList<String>) getIntent().getSerializableExtra("CATEGORIES");
        catAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, recipeCatOptions);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipeCategory.setAdapter(catAdapter);

        recipeCatOptions.add(0, "");
        catAdapter.notifyDataSetChanged();
        recipeCategory.setSelection(0);

        recipeCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i) == recipeCatOptions.get(recipeCatOptions.size() - 1)) {
                    new AddOptionFragment().show(getSupportFragmentManager(), "ADD CATEGORY");
                } else if (adapterView.getItemAtPosition(i) != "" && recipeCatOptions.contains("")) {
                    recipeCatOptions.remove("");
                    catAdapter.notifyDataSetChanged();
                    recipeCategory.setSelection(i - 1);
                } else {
                    catAdapter.notifyDataSetChanged();
                    recipeCategory.setSelection(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                ingredientList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String description = (String) doc.getData().get("description");
                    float amount = Float.parseFloat(String.valueOf(doc.getData().get("amount")));
                    String unit = (String) doc.getData().get("unit");
                    String category = (String) doc.getData().get("category");
                    String date = (String) doc.getData().get("date");
                    String location = (String) doc.getData().get("location");

                    ingredientList.add(new StoredIngredient(description, amount, unit, category, date, location));
                }
                ingredientAdapter.notifyDataSetChanged();
            }
        });

        recipeIngredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickedStoredIngredient = (StoredIngredient) ingredientAdapter.getItem(i);
                Ingredient newIngredient = new Ingredient(clickedStoredIngredient.getDescription(), clickedStoredIngredient.getAmount(), clickedStoredIngredient.getUnit(), clickedStoredIngredient.getCategory());
                if (!addedIngredientList.contains(newIngredient)) {
                    addedIngredientList.add(newIngredient);
                }
                addedIngredientAdapter.notifyDataSetChanged();
            }
        });

        final String addOption = "Add new item";
        //  fetches Categories from Firestore
        ArrayList<String> ingredientCatOptions = new ArrayList<>();
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
                    ingredientCatOptions.clear();
                    ingredientCatOptions.add(addOption);
                    ingredientCatOptions.addAll(0, Objects.requireNonNull(snapshot.getData()).keySet());
                    Log.d("category data", "Current data: " + snapshot.getData());
                } else {
                    Log.d("category data", "Current data: null");
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

        recipeAddedIngredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), EditIngredientInRecipeActivity.class);
                clickedIngredient = (Ingredient) addedIngredientAdapter.getItem(i);
                intent.putExtra("INGREDIENT", clickedIngredient);
                intent.putExtra("CATEGORIES", ingredientCatOptions);
                intent.putExtra("UNITS", unitOptions);
                activityResultLauncher.launch(intent);

            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    recipeImage.setImageBitmap(bitmap);
                }

                if (result.getResultCode() == 401) {
                    Intent intent = result.getData();
                    addedIngredientList.remove(clickedIngredient);
                    clickedIngredient = new Ingredient(intent.getStringExtra("description"), intent.getFloatExtra("amount", 0.0f), intent.getStringExtra("unit"), intent.getStringExtra("category"));
                    addedIngredientList.add(clickedIngredient);

                    addedIngredientAdapter.notifyDataSetChanged();
                }

                if (result.getResultCode() == 402) {
                    addedIngredientList.remove(clickedIngredient);
                    addedIngredientAdapter.notifyDataSetChanged();
                }

            }
        });
        recipeImage = findViewById(R.id.recipeAddImage);
        recipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    activityResultLauncher.launch(intent);
                } else {
                    Toast.makeText(AddRecipeActivity.this, "No camera found!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        FloatingActionButton addRecipeButton = findViewById(R.id.recipeAddConfirm);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                String title = recipeTitle.getText().toString();
                String time = recipeTime.getText().toString();
                String servings = recipeServings.getText().toString();
                String category = recipeCategory.getSelectedItem().toString();
                String comments = recipeComments.getText().toString();

                if (!title.equals("")) {
                    intent.putExtra("title", title);
                } else {
                    title = "Unnamed Recipe";
                    intent.putExtra("title", title );
                }
                if (!time .equals("")) {
                    intent.putExtra("time", Integer.valueOf(time));
                } else {
                    time = "0";
                    intent.putExtra("time", time);
                }
                if (!servings.equals("")) {
                    intent.putExtra("servings", Float.valueOf(servings));
                } else {
                    servings = "0";
                    intent.putExtra("servings", Float.valueOf(servings));
                }
                if (!category.equals("")) {
                    intent.putExtra("category", category);
                } else {
                    category = "Uncategorized";
                    intent.putExtra("category", category);
                }
                if (!comments.equals("")) {
                    intent.putExtra("comments", comments);
                } else {
                    comments = "";
                    intent.putExtra("comments", comments);
                }

                Bitmap bitmap = ((BitmapDrawable) recipeImage.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, stream); //THE QUALITY HERE IS THE 2nd PARAMETER, THINK IT GOES FROM 1-100? MESS AROUND WITH IF NEEDED
                byte[] imageByteArray = stream.toByteArray();
                intent.putExtra("image", imageByteArray);
                //_____________CORRESPONDS TO LINES IN RECIPELISTACTIVITY's activityResultLauncher_____________

                Bundle args = new Bundle();
                args.putSerializable("INGREDIENTSLIST", addedIngredientList);
                intent.putExtra("BUNDLE", args);


                setResult(420, intent);

                //check for what qualifies as an ingredient entry
                if (!(title == "Unnamed Recipe")) {
                    //adding to db
                    Recipe newRecipe = new Recipe(title, Integer.valueOf(time),  Float.valueOf(servings), category, comments, imageByteArray, (ArrayList<Ingredient>) args.getSerializable("INGREDIENTSLIST"));
                    db.addRecipe(newRecipe);
                } else {
                    CharSequence text = "Missing recipe name, failure to add.";
                    Toast.makeText(AddRecipeActivity.this, text, Toast.LENGTH_SHORT).show();
                }

                finish();

            }
        });
    }

    public void onAddOKPressed(String option, int tag) {
        if (option != null) {
            if (tag == 1) {
                db.addRecipeCategory(option);
                recipeCatOptions.remove("");
                recipeCatOptions.add(0, option);
                catAdapter.notifyDataSetChanged();
                recipeCategory.setSelection(0);
            }
        }
    }
}
