package com.example.menuscript;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 *  This class displays the details of an Ingredient.
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
public class ViewIngredientActivity extends AppCompatActivity {

    private EditText ingredientDescription;
    private EditText ingredientAmount;
    private EditText ingredientDate;
    private EditText ingredientUnit;
    private Spinner ingredientLocation;
    private Spinner ingredientCategory;

    ArrayAdapter<String> locAdapter;
    ArrayAdapter<String> catAdapter;

    private static final String[] locOptions = {"Pantry", "Fridge", "Freezer"};
    private static final String[] catOptions = {"Protein", "Carb", "Veg"};

    Calendar calendar = Calendar.getInstance();

    private void updateLabel() {
        String format = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CANADA);

        ingredientDate.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_ingredients);

        ingredientDescription = findViewById(R.id.itemDescriptionEditText);
        ingredientAmount = findViewById(R.id.countEditText);
        ingredientDate = findViewById(R.id.bestBeforeEditText);
        ingredientUnit = findViewById(R.id.unitEditText);
        ingredientLocation = (Spinner) findViewById(R.id.locationSpinner);
        ingredientCategory = (Spinner) findViewById(R.id.categorySpinner);

        StoredIngredient viewedIngredient = (StoredIngredient) getIntent().getSerializableExtra("INGREDIENT");

        ingredientDescription.setText(viewedIngredient.getDescription());
        ingredientAmount.setText((String.valueOf(viewedIngredient.getAmount())));
        ingredientDate.setText(viewedIngredient.getDate());
        ingredientUnit.setText(viewedIngredient.getUnit());

        locAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locOptions);
        catAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catOptions);

        locAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent = onButtonClick(intent);
                setResult(401, intent);
                finish();

            }
        });

        Button deleteButton = findViewById(R.id.deleteButton);
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
        if(!ingredientUnit.getText().toString().equals("")){
            intent.putExtra("unit", ingredientUnit.getText().toString());
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
