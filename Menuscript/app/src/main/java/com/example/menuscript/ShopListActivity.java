package com.example.menuscript;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShopListActivity extends AppCompatActivity {
    ArrayAdapter<Ingredient> shoppingAdapter;
    ArrayList<Ingredient> ingredientList;
    ListView shoppingList;
    Ingredient selectedIngredient;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private FirebaseFirestore databaseInstance;
    private CollectionReference collectionReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoplist_main);

        ingredientList = new ArrayList<Ingredient>();
        shoppingList = findViewById(R.id.shopListMainIngredients);
        shoppingAdapter = new ShopListAdapter(this, ingredientList);
        shoppingList.setAdapter(shoppingAdapter);
        databaseInstance = FirebaseFirestore.getInstance();
        collectionReference = databaseInstance.collection("MealPlanIngredients");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                ingredientList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String title = (String) doc.getData().get("description");

                    ingredientList.add(new Ingredient(title, 0, "", ""));
                }
                shoppingAdapter.notifyDataSetChanged();
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getData() != null){
                    Intent intent = result.getData();
                    String title = intent.getStringExtra("title");
                    String unit = intent.getStringExtra("unit");
                    float servings = intent.getFloatExtra("servings",0.0f);
                    String category = intent.getStringExtra("category");
                    Log.d("CREATION", Integer.toString(result.getResultCode()));
//                    if(result.getResultCode() == 420) {
//                        Ingredient newIngredient = new Ingredient(title, unit, servings, category);
//                        ingredientList.add(newIngredient);
//
//                    } else if (result.getResultCode() == 421){
//
//                    } else if (result.getResultCode() == 422){
//                        ingredientList.remove(selectedIngredient);
//                    }
                    shoppingAdapter.notifyDataSetChanged();

                }
            }
        });

        shoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ShopListActivity.this, ViewShopListIngredientActivity.class);
                selectedIngredient = shoppingAdapter.getItem(i);
                intent.putExtra("NAME", selectedIngredient.getDescription());

                activityResultLauncher.launch(intent);
            }
        });
    }
}
