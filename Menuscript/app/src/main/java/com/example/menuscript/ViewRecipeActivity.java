package com.example.menuscript;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * This class views the details of a recipe, the intent is passed the details of the recipe:
 * recipeName {@link TextView}
 * recipePrepTime {@link TextView}
 * recipeServings {@link TextView}
 * recipeCategory {@link TextView}
 * recipeIngredientList {@link ListView}
 * recipeComments {@link TextView}
 * recipeImage {@link ImageView}
 * noArgument {@link String}
 * ingredientList {@link ArrayList<Ingredient>}
 * ingredientAdapter{@link ArrayAdapter<Ingredient>}
 *
 * @author Wanlin
 */
public class ViewRecipeActivity extends AppCompatActivity {

    TextView recipeName;
    TextView recipePrepTime;
    TextView recipeServings;
    TextView recipeCategory;
    ListView recipeIngredientList;
    TextView recipeComments;
    ImageView recipeImage;
    String noArgument = "Unavailable";
    ArrayList<Ingredient> ingredientList;
    ArrayAdapter<Ingredient> ingredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_edit);
        recipeName = findViewById(R.id.recipeEditName);
        recipePrepTime = findViewById(R.id.recipeEditPrepTime);
        recipeServings = findViewById(R.id.recipeEditServings);
        recipeCategory = findViewById(R.id.recipeEditCategory);
        recipeIngredientList = findViewById(R.id.recipeEditIngredientList);
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

        Bundle args = getIntent().getBundleExtra("INGREDIENTS_BUNDLE");
        ingredientList = (ArrayList<Ingredient>) args.getSerializable("INGREDIENTS");
        //TEMPORARY VIEW OF INGREDIENT LIST
        ingredientAdapter = new RecipeIngredientListAdapter(this, ingredientList);
        recipeIngredientList.setAdapter(ingredientAdapter);


    }
}


