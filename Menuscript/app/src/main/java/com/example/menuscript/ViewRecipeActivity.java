package com.example.menuscript;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * This class views the details of a recipe, the intent is passed the details of the recipe:
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
 * @author Wanlin
 *
 */
public class ViewRecipeActivity extends AppCompatActivity {

    EditText recipeName;
    EditText recipePrepTime;
    EditText recipeServings;
    EditText recipeCategory;
    ListView recipeIngredientList;
    ListView addedRecipeIngredientList;
    EditText recipeComments;
    ImageButton recipeImage;
    String noArgument = "Unavailable";
    ArrayList<Ingredient> ingredientList;
    ArrayAdapter<Ingredient> ingredientAdapter;
    ActivityResultLauncher<Intent> activityResultLauncher;

    ArrayList<Ingredient> addedIngredientList;
    ArrayAdapter<Ingredient> addedIngredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_edit);
        recipeName = findViewById(R.id.recipeEditName);
        recipePrepTime = findViewById(R.id.recipeEditPrepTime);
        recipeServings = findViewById(R.id.recipeEditServings);
        recipeCategory = findViewById(R.id.recipeEditCategory);
        recipeIngredientList = findViewById(R.id.recipeEditIngredientList);
        addedRecipeIngredientList = findViewById(R.id.recipeEditIngredientListAdded);
        recipeComments = findViewById((R.id.recipeEditComment));
        recipeImage = findViewById(R.id.recipeEditImage);





        Bundle bundle = getIntent().getExtras();

        if (bundle.getString("NAME")!=null){
            recipeName.setText(bundle.getString("NAME"));
        }else{
            recipeName.setText(noArgument);
        }

        if (String.valueOf(bundle.getInt("TIME"))!=null){
            recipePrepTime.setText(String.valueOf(bundle.getInt("TIME")));
        }else{
            recipePrepTime.setText(noArgument);
        }

        if (String.valueOf(bundle.getFloat("SERVINGS"))!=null){
            recipeServings.setText(String.valueOf(bundle.getFloat("SERVINGS")));
        }else{
            recipeServings.setText(noArgument);
        }

        if (bundle.getString("CATEGORY")!=null){
            recipeCategory.setText(bundle.getString("CATEGORY"));
        }else{
            recipeCategory.setText(noArgument);
        }

        recipeComments.setMovementMethod(new ScrollingMovementMethod());
        if (bundle.getString("COMMENTS")!=null){
            recipeComments.setText(bundle.getString("COMMENTS"));
        }else{
            recipeName.setText(noArgument);
        }

        if (bundle.getString("COMMENTS")!=null){
            recipeComments.setText(bundle.getString("COMMENTS"));
        }else{
            recipeName.setText(noArgument);
        }

        if (bundle.getByteArray("IMAGE") != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(bundle.getByteArray("IMAGE"), 0, bundle.getByteArray("IMAGE").length);
            recipeImage.setImageBitmap(bitmap);
        }


        ingredientList = new ArrayList<Ingredient>();
        Ingredient test1 = new Ingredient( "Pickles", 12, "pounds", "Vegetable");
        Ingredient test2 = new Ingredient("ThisIsToTestVeryLongCharacterStringsLikeReallyReallyReallyLongOnesIsThisLongEnough?", 12, "unit", "TestReallyLongCategories");
        Ingredient test3 = new Ingredient("Blue Rice", 12, "pounds", "Carb");
        ingredientList.add(test1);
        ingredientList.add(test2);
        ingredientList.add(test3); //THIS IS TEMP REPRESENTATION OF ALL INGREDIENTS IN STORAGE
        ingredientAdapter = new RecipeIngredientListAdapter(this, ingredientList);
        recipeIngredientList.setAdapter(ingredientAdapter);

        Bundle args = getIntent().getBundleExtra("INGREDIENTS_BUNDLE");
        addedIngredientList = (ArrayList<Ingredient>) args.getSerializable("INGREDIENTS");
        addedIngredientAdapter = new RecipeIngredientListAdapter(this, addedIngredientList);
        addedRecipeIngredientList.setAdapter(addedIngredientAdapter);


        recipeIngredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ingredient movedIngredient = ingredientList.get(i);
                addedIngredientList.add(movedIngredient);
                addedIngredientAdapter.notifyDataSetChanged();
            }
        });

        addedRecipeIngredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addedIngredientList.remove(addedIngredientList.get(i));
                addedIngredientAdapter.notifyDataSetChanged();
            }
        });


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

        recipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null){
                    activityResultLauncher.launch(intent);
                } else {
                    Toast.makeText(ViewRecipeActivity.this,"No camera found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

        FloatingActionButton recipeDelete = findViewById(R.id.recipeDelete);
        recipeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent = onButtonClick(intent);
                setResult(422,intent);
                finish();
            }
        });
    }

    private Intent onButtonClick(Intent intent){

        if(!recipeName.getText().toString().equals("")) {
            intent.putExtra("title", recipeName.getText().toString());
        } else {
            intent.putExtra("title", "Unnamed Recipe");
        }
        if(!recipePrepTime.getText().toString().equals("")) {
            intent.putExtra("time", Integer.valueOf(recipePrepTime.getText().toString()));
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
        Bitmap bitmap = ((BitmapDrawable)recipeImage.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,10,stream);
        byte[] imageByteArray = stream.toByteArray();
        intent.putExtra("image", imageByteArray);

        return intent;
    }
}