package com.example.menuscript;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {
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
        shoppingAdapter = new ShoppingListAdapter(this, ingredientList);
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

        shoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ShoppingListActivity.this, ViewShoppingListIngredientActivity.class);
                selectedIngredient = shoppingAdapter.getItem(i);
                intent.putExtra("NAME", selectedIngredient.getDescription());
                intent.putExtra("AMOUNT", selectedIngredient.getAmount());
                intent.putExtra("CATEGORY", selectedIngredient.getCategory());
                intent.putExtra("UNIT", selectedIngredient.getUnit());

                activityResultLauncher.launch(intent);
            }
        });
    }
}
