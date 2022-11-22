package com.example.menuscript;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
/**
 * AddIngredientActivity displays multiple fields that are editable for users.
 * The activity returns all attributes from the user for that instance of the Ingredient object.
 * ingredientDescription {@link EditText}
 * ingredientAmount {@link EditText}
 * ingredientUnit {@link EditText}
 * ingredientDate {@link EditText}
 * ingredientLocation {@link Spinner}
 * ingredientCategory {@link Spinner}
 *
 * @see Ingredient
 * @see StoredIngredient
 * @see IngredientListActivity
 */

public class AddIngredientActivity extends AppCompatActivity {

    private EditText ingredientDescription;
    private EditText ingredientAmount;
    private Spinner ingredientUnit;
    private EditText ingredientDate;
    private Spinner ingredientLocation;
    private Spinner ingredientCategory;

    ArrayAdapter<String> locAdapter;
    ArrayAdapter<String> catAdapter;

    private static final String[] locOptions = {"Pantry", "Fridge", "Freezer"};
    private static final String[] catOptions = {"Protein", "Carb", "Veg"};

    Calendar calendar = Calendar.getInstance();

    /**
     * Obtains date from the user to set date for the date attribute in the ingredient class.
     * format {@link String}
     *
     * @see Ingredient
     */
    private void updateLabel() {
        String format = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CANADA);

        ingredientDate.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_ingredients);

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setVisibility(View.INVISIBLE);

        ingredientDescription = findViewById(R.id.itemDescriptionEditText);
        ingredientAmount = findViewById(R.id.countEditText);
        ingredientUnit = findViewById(R.id.unitSpinner);
        ingredientDate = findViewById(R.id.bestBeforeEditText);
        ingredientLocation = findViewById(R.id.locationSpinner);
        ingredientCategory = findViewById(R.id.categorySpinner);

        locAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locOptions);
        catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, catOptions);

        ingredientLocation.setAdapter(locAdapter);
        ingredientCategory.setAdapter(catAdapter);

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
                    new DatePickerDialog(AddIngredientActivity.this, datePicker, calendar
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

        Button addIngredientButton = findViewById(R.id.submitButton);
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent = onButtonClick(intent);
                setResult(400, intent);
                finish();

            }
        });
    }
    private Intent onButtonClick(Intent intent){

        if(!ingredientDescription.getText().toString().equals("")) {
            intent.putExtra("description", ingredientDescription.getText().toString());
        } else {
            intent.putExtra("description", "Unnamed Ingredient");
        }
        if(!ingredientAmount.getText().toString().equals("")) {
            intent.putExtra("amount", Float.valueOf(ingredientAmount.getText().toString()));
        } else {
            intent.putExtra("amount", 0.0f);
        }
        if(!ingredientUnit.getSelectedItem().toString().equals("")){
            intent.putExtra("unit", ingredientUnit.getSelectedItem().toString());
        } else {
            intent.putExtra("unit","No Unit");
        }
        if(!ingredientDate.getText().toString().equals("")){
            intent.putExtra("date", ingredientDate.getText().toString());
        } else {
            intent.putExtra("category","No Best Before Date");
        }
        if(!ingredientCategory.getSelectedItem().toString().equals("")) {
            intent.putExtra("category",ingredientCategory.getSelectedItem().toString());
        } else {
            intent.putExtra("category","Uncategorized");
        }
        if(!ingredientLocation.getSelectedItem().toString().equals("")) {
            intent.putExtra("location",ingredientLocation.getSelectedItem().toString());
        } else {
            intent.putExtra("location","No Location");
        }
        return intent;
    }
}