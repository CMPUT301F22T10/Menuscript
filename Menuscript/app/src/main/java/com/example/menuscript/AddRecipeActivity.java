package com.example.menuscript;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

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
 * ingredientListView {@link ListView}
 * ingredientAdapter {@link CustomIngredientList}
 * ingredientList {@link ListView}
 *
 * @author Dylan Clarke
 */

public class AddRecipeActivity extends AppCompatActivity {
    EditText recipeTitle;
    EditText recipeTime;
    EditText recipeServings;
    EditText recipeCategory;
    EditText recipeComments;
    ImageButton recipeImage;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ListView ingredientListView;
    CustomIngredientList ingredientAdapter;
    ArrayList<Ingredient> ingredientList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_add);

        recipeTitle = findViewById(R.id.recipeAddTitle);
        recipeTime = findViewById(R.id.recipeAddTime);
        recipeServings = findViewById(R.id.recipeAddServings);
        recipeCategory = findViewById(R.id.recipeAddCategory);
        recipeComments = findViewById(R.id.recipeAddComments);

        ingredientList = (ArrayList<Ingredient>) getIntent().getSerializableExtra("ingredients");
        ingredientListView = findViewById(R.id.recipeAddIngredientsList);
        ingredientAdapter = new CustomIngredientList(this,ingredientList);
        ingredientListView.setAdapter(ingredientAdapter);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK && result.getData() != null){
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    recipeImage.setImageBitmap(bitmap);
                }
            }
        });
        recipeImage = findViewById(R.id.recipeAddImage);
        recipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null){
                    intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    activityResultLauncher.launch(intent);
                } else {
                    Toast.makeText(AddRecipeActivity.this,"No camera found!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        FloatingActionButton addRecipeButton = findViewById(R.id.recipeAddConfirm);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if(!recipeTitle.getText().toString().equals("")) {
                    intent.putExtra("title", recipeTitle.getText().toString());
                } else {
                    intent.putExtra("title", "Unnamed Recipe");
                }
                if(!recipeTime.getText().toString().equals("")) {
                    intent.putExtra("time", Integer.valueOf(recipeTime.getText().toString()));
                } else {
                    intent.putExtra("time", 0);
                }
                if(!recipeServings.getText().toString().equals("")){
                    intent.putExtra("servings",Float.valueOf(recipeServings.getText().toString()));
                } else {
                    intent.putExtra("servings",0f);
                }
                if(!recipeCategory.getText().toString().equals("")){
                    intent.putExtra("category",recipeCategory.getText().toString());
                } else {
                    intent.putExtra("category","Uncategorized");
                }
                if(!recipeComments.getText().toString().equals("")) {
                    intent.putExtra("comments",recipeComments.getText().toString());
                } else {
                    intent.putExtra("comments","");
                }


                //_____________CHECK IF IMAGE FUNCTIONALITY WORKING______________
                Bitmap bitmap = ((BitmapDrawable)recipeImage.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,10,stream); //THE QUALITY HERE IS THE 2nd PARAMETER, THINK IT GOES FROM 1-100? MESS AROUND WITH IF NEEDED
                byte[] imageByteArray = stream.toByteArray();
                intent.putExtra("image", imageByteArray);
                //_____________CORRESPONDS TO LINES IN RECIPELISTACTIVITY's activityResultLauncher_____________
                setResult(420,intent);
                finish();

            }
        });
    }
}
