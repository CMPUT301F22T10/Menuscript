package com.example.menuscript;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * This class displays the details of an Ingredient.
 * ingredientDescription {@link EditText}
 * ingredientAmount {@link EditText}
 * ingredientDate {@link EditText}
 * ingredientUnit {@link EditText}
 * ingredientLocation {@link Spinner}
 * ingredientCategory {@link Spinner}
 * calendar {@link Calendar}
 *
 * @see Ingredient
 */
public class ViewIngredientActivity extends AppCompatActivity implements AddOptionFragment.OnFragmentInteractionListener {


    private EditText ingredientDescription;

    private EditText ingredientAmount;
    private EditText ingredientDate;
    private Spinner ingredientUnit;
    private Spinner ingredientLocation;
    private Spinner ingredientCategory;

    ArrayAdapter<String> locAdapter;
    ArrayAdapter<String> catAdapter;
    ArrayAdapter<String> unitAdapter;

    ArrayList<String> locOptions;
    ArrayList<String> catOptions;
    ArrayList<String> unitOptions;

    Calendar calendar = Calendar.getInstance();

    DatabaseManager db = new DatabaseManager(this);

    private void updateLabel() {
        String format = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CANADA);

        ingredientDate.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_ingredients);

        FirebaseFirestore databaseInstance = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = databaseInstance.collection("Options");

        ingredientDescription = findViewById(R.id.itemDescriptionEditText);
        ingredientAmount = findViewById(R.id.countEditText);
        ingredientDate = findViewById(R.id.bestBeforeEditText);
        ingredientUnit = findViewById(R.id.unitSpinner);
        ingredientLocation = findViewById(R.id.locationSpinner);
        ingredientCategory = findViewById(R.id.categorySpinner);

        StoredIngredient viewedIngredient = (StoredIngredient) getIntent().getSerializableExtra("INGREDIENT");

        catOptions = (ArrayList<String>) getIntent().getSerializableExtra("CATEGORIES");
        locOptions = (ArrayList<String>) getIntent().getSerializableExtra("LOCATIONS");
        unitOptions = (ArrayList<String>) getIntent().getSerializableExtra("UNITS");

        ingredientDescription.setText(viewedIngredient.getDescription());
        ingredientAmount.setText((String.valueOf(viewedIngredient.getAmount())));
        ingredientDate.setText(viewedIngredient.getDate());

        locAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locOptions);
        catAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catOptions);
        unitAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unitOptions);

        locAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ingredientLocation.setAdapter(locAdapter);
        ingredientCategory.setAdapter(catAdapter);
        ingredientUnit.setAdapter(unitAdapter);

        if (viewedIngredient.getCategory() != null) {
            ingredientCategory.setSelection(catOptions.indexOf(viewedIngredient.getCategory()));
        } else if (!catOptions.contains(viewedIngredient.getCategory())) {
            catOptions.add(0, viewedIngredient.getCategory());
            catAdapter.notifyDataSetChanged();
            ingredientCategory.setSelection(0);
        } else {
            catOptions.add(0, "");
            catAdapter.notifyDataSetChanged();
            ingredientCategory.setSelection(0);
        }


        if (viewedIngredient.getLocation() != null) {
            ingredientLocation.setSelection(locOptions.indexOf(viewedIngredient.getLocation()));
        }  else if (!locOptions.contains(viewedIngredient.getLocation())) {
            locOptions.add(0, viewedIngredient.getCategory());
            locAdapter.notifyDataSetChanged();
            ingredientLocation.setSelection(0);
        } else {
            locOptions.add(0, "");
            locAdapter.notifyDataSetChanged();
            ingredientLocation.setSelection(0);
        }

        if (viewedIngredient.getUnit() != null) {
            ingredientUnit.setSelection(unitOptions.indexOf(viewedIngredient.getUnit()));
        }  else if (!unitOptions.contains(viewedIngredient.getUnit())) {
            unitOptions.add(0, viewedIngredient.getCategory());
            unitAdapter.notifyDataSetChanged();
            ingredientUnit.setSelection(0);
        }  else {
            unitOptions.add(0, "");
            unitAdapter.notifyDataSetChanged();
            ingredientUnit.setSelection(0);
        }

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
                    new DatePickerDialog(ViewIngredientActivity.this, datePicker, calendar
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


        ingredientCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i) == catOptions.get(catOptions.size() - 1)) {
                    new AddOptionFragment().show(getSupportFragmentManager(), "ADD CATEGORY");
                } else {
                    ingredientCategory.setSelection(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ingredientLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i) == locOptions.get(locOptions.size() - 1)) {
                    new AddOptionFragment().show(getSupportFragmentManager(), "ADD LOCATION");
                } else {
                    ingredientLocation.setSelection(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ingredientUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i) == unitOptions.get(unitOptions.size() - 1)) {
                    new AddOptionFragment().show(getSupportFragmentManager(), "ADD UNIT");
                } else {
                    ingredientUnit.setSelection(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                onButtonClick(intent);
                setResult(401, intent);
                finish();

            }
        });

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                onButtonClick(intent);
                setResult(402, intent);
                finish();

            }
        });
    }

    private void onButtonClick(Intent intent) {

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
        if (!ingredientUnit.getSelectedItem().toString().equals("")) {
            intent.putExtra("unit", ingredientUnit.getSelectedItem().toString());
        } else {
            intent.putExtra("unit", "No Unit");
        }
        if (!ingredientDate.getText().toString().equals("")) {
            intent.putExtra("date", ingredientDate.getText().toString());
        } else {
            intent.putExtra("category", "No Best Before Date");
        }
        if (!ingredientCategory.getSelectedItem().toString().equals("")) {
            intent.putExtra("category", ingredientCategory.getSelectedItem().toString());
        } else {
            intent.putExtra("category", "Uncategorized");
        }
        if (!ingredientLocation.getSelectedItem().toString().equals("")) {
            intent.putExtra("location", ingredientLocation.getSelectedItem().toString());
        } else {
            intent.putExtra("location", "No Location");
        }
    }

    public void onAddOKPressed(String option, int tag) {
        if (option != null) {
            if (tag == 1) {
                db.addIngredientCategory(option);
                catOptions.remove("");
                catOptions.add(0, option);
                catAdapter.notifyDataSetChanged();
                ingredientCategory.setSelection(0);
            } else if (tag == 2) {
                db.addLocation(option);
                locOptions.remove("");
                locOptions.add(0, option);
                locAdapter.notifyDataSetChanged();
                ingredientLocation.setSelection(0);
            } else {
                db.addUnit(option);
                unitOptions.remove("");
                unitOptions.add(0, option);
                unitAdapter.notifyDataSetChanged();
                ingredientUnit.setSelection(0);
            }
        }
    }
}
