package com.example.menuscript;


import static android.content.ContentValues.TAG;

import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private FirebaseFirestore databaseInstance;
    private CollectionReference collectionReference;

    public DatabaseManager () {
        this.databaseInstance = FirebaseFirestore.getInstance();
    }

    /**
     * Returns an array from database of user stored ingredients.
     */
    public ArrayList<StoredIngredient> getStoredIngredients () {
        ArrayList<StoredIngredient> ingredients = new ArrayList<StoredIngredient>();
        collectionReference = databaseInstance.collection("StoredIngredients");
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                    public void onComplete (@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> data = document.getData();
                            ingredients.add(new StoredIngredient(
                                    (String)data.get("description"),
                                    (float)data.get("amount"),
                                    (String)data.get("unit"),
                                    (String)data.get("category"),
                                    (String)data.get("date"),
                                    (String)data.get("location")
                            ));
                        }
                    } else {
                        Log.d(TAG, "Error Getting StoredIngredients from database.");
                    }
                }
        });

        return ingredients;
    }

    public void addStoredIngredient (StoredIngredient storedIngredient) {

    }

    public void deleteStoredIngredient (StoredIngredient storedIngredient) {

    }

    public ArrayList<Recipe> getRecipes () {
        return new ArrayList<Recipe>();
    }

    public void addRecipe (Recipe recipe) {
        ;
    }

    public void deleteRecipe (Recipe recipe) {
        ;
    }

    public ArrayList<String> getIngredientCategories () {
        ArrayList<String> cats = new ArrayList<String>();
        cats.add("Breakfast");
        cats.add("Lunch");
        cats.add("Dinner");
        return cats;
    }
}
