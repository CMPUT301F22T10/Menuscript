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
 * @author Dylan Clarke
 */

public class AddRecipeActivity extends AppCompatActivity {
    private EditText recipeTitle;
    private EditText recipeTime;
    private EditText recipeServings;
    private EditText recipeCategory;
    private EditText recipeComments;
    private ImageButton recipeImage;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ListView ingredientListView;
    private CustomIngredientList ingredientAdapter;
    private ArrayList<Ingredient> ingredientList;

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
                intent.putExtra("title",recipeTitle.getText().toString());
                intent.putExtra("time",Integer.valueOf(recipeTime.getText().toString()));
                intent.putExtra("servings",Float.valueOf(recipeServings.getText().toString()));
                intent.putExtra("category",recipeCategory.getText().toString());
                intent.putExtra("comments",recipeComments.getText().toString());

                //_____________CHECK IF IMAGE FUNCTIONALITY WORKING______________
                //Bitmap bitmap = ((BitmapDrawable)recipeImage.getDrawable()).getBitmap();
                //ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //bitmap.compress(Bitmap.CompressFormat.PNG,10,stream); //THE QUALITY HERE IS THE 2nd PARAMETER, THINK IT GOES FROM 1-100? MESS AROUND WITH IF NEEDED
                //byte[] imageByteArray = stream.toByteArray();
                //intent.putExtra("image", imageByteArray);
                //_____________CORRESPONDS TO LINES IN RECIPELISTACTIVITY's activityResultLauncher_____________
                setResult(420,intent);
                finish();

            }
        });
    }
}
