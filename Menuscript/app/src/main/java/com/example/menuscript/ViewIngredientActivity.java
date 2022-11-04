package com.example.menuscript;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

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
        ingredientLocation.setonItemSelectedListener(this);
        ingredientCategory.setonItemSelectedListener(this);
        locAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locOptions);
        locAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredientLocation.setAdapter(locAdapter);
        catAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catOptions);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredientCategory.setAdapter(catAdapter);
        FloatingActionButton editRecipeButton = findViewById(R.id.recipeConfirmEdit);
        editRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent = onButtonClick(intent);
                setResult(421,intent);
                finish();

            }
        });

    }
}
