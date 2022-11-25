package com.example.menuscript;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;

public class EditIngredientInRecipeActivity extends AppCompatActivity implements AddOptionFragment.OnFragmentInteractionListener  {

    private EditText ingredientDescription;

    private EditText ingredientAmount;
    //private EditText ingredientDate;
    private EditText ingredientUnit;
    //private Spinner ingredientLocation;
    private Spinner ingredientCategory;

    //ArrayAdapter<String> locAdapter;
    ArrayAdapter<String> catAdapter;

    //ArrayList<String> locOptions;
    ArrayList<String> catOptions;

    //Calendar calendar = Calendar.getInstance();

    private FirebaseFirestore databaseInstance;
    private CollectionReference collectionReference;
    DatabaseManager db = new DatabaseManager(this);

    /*
    private void updateLabel() {
        String format = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CANADA);

        ingredientDate.setText(dateFormat.format(calendar.getTime()));
    }
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_ingredientedit);

        databaseInstance = FirebaseFirestore.getInstance();
        collectionReference = databaseInstance.collection("Options");

        ingredientDescription = findViewById(R.id.itemDescriptionEditText_recipe);
        ingredientAmount = findViewById(R.id.countEditText_recipe);
        //ingredientDate = findViewById(R.id.bestBeforeEditText_recipe); //to be excluded
        ingredientUnit = findViewById(R.id.unitEditText_recipe);
        //ingredientLocation = (Spinner) findViewById(R.id.locationSpinner_recipe); //to be excluded
        ingredientCategory = (Spinner) findViewById(R.id.categorySpinner_recipe);

        Ingredient viewedIngredient = (Ingredient) getIntent().getSerializableExtra("INGREDIENT");

        //locOptions = new ArrayList<>();
        catOptions = new ArrayList<>();

        final String addOption = "Add new item";

        //locOptions.add(addOption);
        catOptions.add(0, "");

        final DocumentReference catRef = collectionReference.document("Ingredient Categories");
        catRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
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
                    catOptions.addAll(0, snapshot.getData().keySet());
                    Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }
                catAdapter.notifyDataSetChanged();
            }
        });

        ingredientDescription.setText(viewedIngredient.getDescription());
        ingredientAmount.setText((String.valueOf(viewedIngredient.getAmount())));
        //ingredientDate.setText(viewedIngredient.getDate());
        ingredientUnit.setText(viewedIngredient.getUnit());

        Log.d("i hate tgis", catOptions.toString());
        ingredientCategory.setSelection(catOptions.indexOf(viewedIngredient.getCategory()));


        //locAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locOptions);
        catAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catOptions);

        //locAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //ingredientLocation.setAdapter(locAdapter);
        ingredientCategory.setAdapter(catAdapter);

        /*
        DatePickerDialog.OnDateSetListener datePicker = (datePicker1, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();
        };

        ingredientDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    new DatePickerDialog(EditIngredientInRecipeActivity.this, datePicker, calendar
                            .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                    ).show();
                }
            }
        });



        ingredientDate.setOnClickListener(v -> new DatePickerDialog(this, datePicker, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show());
        */

        ingredientCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i) == addOption) {
                    new AddOptionFragment().show(getSupportFragmentManager(), "ADD CATEGORY");
                } else {
//                    ingredientCategory.setSelection(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button submitButton = findViewById(R.id.submitButton_recipe);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent = onButtonClick(intent);
                setResult(401, intent);
                finish();

            }
        });

        Button deleteButton = findViewById(R.id.deleteButton_recipe);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent = onButtonClick(intent);
                setResult(402, intent);
                finish();

            }
        });
    }

    private Intent onButtonClick(Intent intent) {

        if (!ingredientDescription.getText().toString().equals("")) {
            intent.putExtra("description", ingredientDescription.getText().toString());
        } else {
            intent.putExtra("description", "Unnamed Ingredient");
        }
        if (!ingredientAmount.getText().toString().equals("")) {
            intent.putExtra("amount", Float.valueOf(ingredientAmount.getText().toString()));
        } else {
            intent.putExtra("amount", 0.0f);
        }
        if (!ingredientUnit.getText().toString().equals("")) {
            intent.putExtra("unit", ingredientUnit.getText().toString());
        } else {
            intent.putExtra("unit", "No Unit");
        }
        if (!ingredientCategory.getSelectedItem().toString().equals("")) {
            intent.putExtra("category", ingredientCategory.getSelectedItem().toString());
        } else {
            intent.putExtra("category", "Uncategorized");
        }
        return intent;
    }

    public void onAddOKPressed(String option) {
        if (option != null) {
            db.addIngredientCategory(option);
            catAdapter.notifyDataSetChanged();
        }
    }
}

