package com.example.menuscript;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

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
import java.util.Comparator;

public class ShopListActivity extends AppCompatActivity {
    ArrayAdapter<Ingredient> shoppingAdapter;
    ArrayList<Ingredient> ingredientList;
    ArrayList<String> ingredientNameList;
    ListView shoppingList;
    Ingredient selectedIngredient;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private FirebaseFirestore databaseInstance;
    private CollectionReference mealPlanCollection;
    private CollectionReference ingredientCollection;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoplist_main);

        ingredientList = new ArrayList<Ingredient>();
        ingredientNameList = new ArrayList<String>();
        shoppingList = findViewById(R.id.shopListMainIngredients);
        shoppingAdapter = new ShopListAdapter(this, ingredientList);
        shoppingList.setAdapter(shoppingAdapter);
        databaseInstance = FirebaseFirestore.getInstance();
        mealPlanCollection = databaseInstance.collection("MealPlanIngredients");
        ingredientCollection = databaseInstance.collection("StoredIngredients");

        mealPlanCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                ingredientNameList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String name = (String) doc.getData().get("description");
                    ingredientNameList.add(name);
                }
                shoppingAdapter.notifyDataSetChanged();
            }
        });

        ingredientCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                ingredientList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String ingredientName = (String) doc.getData().get("description");
                    if (ingredientNameList.contains(ingredientName)) {
                        String name = (String) doc.getData().get("description");
                        String unit = (String) doc.getData().get("unit");
                        float servings = Float.parseFloat(String.valueOf((doc.getData().get("amount"))));
                        String category = (String) doc.getData().get("category");
                        ingredientList.add(new Ingredient(name, servings, unit, category));
                    }
                }
                shoppingAdapter.notifyDataSetChanged();
            }
        });

        shoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ShopListActivity.this, ViewShopListIngredientActivity.class);
                selectedIngredient = shoppingAdapter.getItem(i);
                intent.putExtra("NAME", selectedIngredient.getDescription());
                intent.putExtra("AMOUNT", selectedIngredient.getAmount());
                intent.putExtra("UNIT", selectedIngredient.getUnit());
                intent.putExtra("CATEGORY", selectedIngredient.getCategory());

                startActivity(intent);
            }
        });

//        Spinner sortCategoryButton = findViewById(R.id.shopListMainSpinner);
//        sortCategoryButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (adapterView.getItemAtPosition(i) == "Category") {
//                    ingredientList.sort(Comparator.comparing(Ingredient::getCategory));
//                    Log.d("TEST", "hi");
//                } else if (adapterView.getItemAtPosition(i) == "Description") {
//                    ingredientList.sort(Comparator.comparing(Ingredient::getDescription));
//                    Log.d("TEST", "bye");
//                }
//                shoppingAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
    }
}
