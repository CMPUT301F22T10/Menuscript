package com.example.menuscript;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class views the details of a recipe, the intent is passed the details of the recipe:
 * This class also serves to edit and delete a Viewed Recipe.
 * Attributes can be edited by clicking on them, and clicking the top-right button confirms the changes.
 * Clicking the top-left button will delete the recipe.
 * Currently, interactions with ingredients is not yet implemented.
 * recipeName {@link EditText}
 * recipePrepTime {@link EditText}
 * recipeServings {@link EditText}
 * recipeCategory {@link EditText}
 * recipeIngredientList {@link ListView}
 * recipeComments {@link EditText}
 * recipeImage {@link ImageButton}
 * noArgument {@link String}
 * ingredientList {@link ArrayList<Ingredient>}
 * ingredientAdapter{@link ArrayAdapter<Ingredient>}
 *
 * @author Wanlin, Dylan
 * @see Recipe
 * @see Ingredient
 * @see StoredIngredient
 */
public class ViewRecipeActivity extends AppCompatActivity implements AddOptionFragment.OnFragmentInteractionListener {

    EditText recipeName;
    EditText recipePrepTime;
    EditText recipeServings;
    Spinner recipeCategory;
    ListView recipeIngredientList;
    ListView recipeAddedIngredientList;
    EditText recipeComments;
    ImageButton recipeImage;
    String noArgument = "Unavailable";
    ArrayList<Ingredient> ingredientList;
    ArrayList<Ingredient> addedIngredientList;
    ArrayAdapter<Ingredient> ingredientAdapter;
    ArrayAdapter<Ingredient> addedIngredientAdapter;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private FirebaseFirestore databaseInstance;
    private CollectionReference collectionReference;

    ArrayAdapter<String> catAdapter;
    ArrayList<String> catOptions;

    StoredIngredient clickedStoredIngredient;
    Ingredient clickedIngredient;

    DatabaseManager db = new DatabaseManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_edit);
        recipeName = findViewById(R.id.recipeEditName);
        recipePrepTime = findViewById(R.id.recipeEditPrepTime);
        recipeServings = findViewById(R.id.recipeEditServings);
        recipeCategory = findViewById(R.id.recipeEditCategory);
        recipeIngredientList = findViewById(R.id.recipeEditIngredientList);
        recipeComments = findViewById((R.id.recipeEditComment));
        recipeImage = findViewById(R.id.recipeEditImage);
        recipeAddedIngredientList = findViewById(R.id.recipeEditIngredientListAdded);

        Bundle bundle = getIntent().getExtras();

        catOptions = (ArrayList<String>) bundle.getSerializable("CATEGORY LIST");
        catAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catOptions);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipeCategory.setAdapter(catAdapter);

        if (bundle.getString("NAME") != null) {
            recipeName.setText(bundle.getString("NAME"));
        } else {
            recipeName.setText(noArgument);
        }

        if (String.valueOf(bundle.getInt("TIME")) != null) {
            recipePrepTime.setText(String.valueOf(bundle.getInt("TIME")));
        } else {
            recipePrepTime.setText(noArgument);
        }

        if (String.valueOf(bundle.getFloat("SERVINGS")) != null) {
            recipeServings.setText(String.valueOf(bundle.getFloat("SERVINGS")));
        } else {
            recipeServings.setText(noArgument);
        }

        String currentCat = bundle.getString("CATEGORY");
        if (currentCat != null && catOptions.contains(currentCat)) {
            recipeCategory.setSelection(catOptions.indexOf(currentCat));
        } else if (!catOptions.contains(currentCat)) {
            catOptions.add(0, currentCat);
            catAdapter.notifyDataSetChanged();
            recipeCategory.setSelection(0);
        } else {
            catOptions.add(0, "");
            catAdapter.notifyDataSetChanged();
            recipeCategory.setSelection(0);
        }

        recipeComments.setMovementMethod(new ScrollingMovementMethod());
        if (bundle.getString("COMMENTS") != null) {
            recipeComments.setText(bundle.getString("COMMENTS"));
        } else {
            recipeName.setText(noArgument);
        }

        if (bundle.getString("COMMENTS") != null) {
            recipeComments.setText(bundle.getString("COMMENTS"));
        } else {
            recipeName.setText(noArgument);
        }

        if (bundle.getByteArray("IMAGE") != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bundle.getByteArray("IMAGE"), 0, bundle.getByteArray("IMAGE").length);
            recipeImage.setImageBitmap(bitmap);
        }


        databaseInstance = FirebaseFirestore.getInstance();
        collectionReference = databaseInstance.collection("StoredIngredients");
        ingredientList = new ArrayList<>();
        ingredientAdapter = new RecipeIngredientListAdapter(this, ingredientList);
        recipeIngredientList.setAdapter(ingredientAdapter);

        addedIngredientList = new ArrayList<>();
        addedIngredientAdapter = new RecipeIngredientListAdapter(this, addedIngredientList);
        recipeAddedIngredientList.setAdapter(addedIngredientAdapter);


        Bundle args = getIntent().getBundleExtra("INGREDIENTS_BUNDLE");
        ArrayList<Ingredient> importedIngredients = (ArrayList<Ingredient>) args.getSerializable("INGREDIENTS");

        addedIngredientList.addAll(importedIngredients);
        addedIngredientAdapter.notifyDataSetChanged();

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

        recipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    activityResultLauncher.launch(intent);
                } else {
                    Toast.makeText(ViewRecipeActivity.this, "No camera found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //  listens for selection of category
        recipeCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i) == catOptions.get(catOptions.size() - 1)) {
                    new AddOptionFragment().show(getSupportFragmentManager(), "ADD CATEGORY");
                } else if (adapterView.getItemAtPosition(i) != "" && catOptions.contains("")) {
                    catOptions.remove("");
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

        FloatingActionButton editRecipeButton = findViewById(R.id.recipeConfirmEdit);
        editRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                onButtonClick(intent);
                setResult(421, intent);
                finish();

            }
        });

        FloatingActionButton recipeDelete = findViewById(R.id.recipeDelete);
        recipeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                onButtonClick(intent);
                setResult(422, intent);
                finish();
            }
        });
    }

    private Intent onButtonClick(Intent intent) {

        if (!recipeName.getText().toString().equals("")) {
            intent.putExtra("title", recipeName.getText().toString());
        } else {
            intent.putExtra("title", "Unnamed Recipe");
        }
        if (!recipePrepTime.getText().toString().equals("")) {
            intent.putExtra("time", Integer.valueOf(recipePrepTime.getText().toString()));
        } else {
            intent.putExtra("time", 0);
        }
        if (!recipeServings.getText().toString().equals("")) {
            intent.putExtra("servings", Float.valueOf(recipeServings.getText().toString()));
        } else {
            intent.putExtra("servings", 0f);
        }
        if (!recipeCategory.getSelectedItem().toString().equals("")) {
            intent.putExtra("category", recipeCategory.getSelectedItem().toString());
        } else {
            intent.putExtra("category", "Uncategorized");
        }
        if (!recipeComments.getText().toString().equals("")) {
            intent.putExtra("comments", recipeComments.getText().toString());
        } else {
            intent.putExtra("comments", "");
        }
        Bitmap bitmap = ((BitmapDrawable) recipeImage.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, stream);
        byte[] imageByteArray = stream.toByteArray();
        intent.putExtra("image", imageByteArray);

        Bundle args = new Bundle();
        args.putSerializable("INGREDIENTSLIST", addedIngredientList);
        intent.putExtra("BUNDLE", args);

        return intent;
    }

    public void onAddOKPressed(String option, int tag) {
        if (option != null) {
            db.addRecipeCategory(option);
            catOptions.remove("");
            catOptions.add(0, option);
            catAdapter.notifyDataSetChanged();
            recipeCategory.setSelection(0);
        }
    }
}