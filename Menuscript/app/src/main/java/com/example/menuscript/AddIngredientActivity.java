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

public class AddIngredientActivity extends AppCompatActivity {

    private EditText ingredientDescription;
    private EditText ingredientAmount;
    private EditText ingredientDate;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_ingredients);

        ingredientDescription = findViewById(R.id.itemDescriptionEditText);
        ingredientAmount = findViewById(R.id.countEditText);
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
                intent.putExtra("description", ingredientDescription.getText().toString());
                intent.putExtra("amount", Integer.valueOf(ingredientAmount.getText().toString()));
                intent.putExtra("date", ingredientDate.getText().toString());
                intent.putExtra("location", ingredientLocation.getSelectedItem().toString());
                intent.putExtra("category", ingredientCategory.getSelectedItem().toString());

                setResult(6969, intent);
                finish();

            }
        });
    }
}