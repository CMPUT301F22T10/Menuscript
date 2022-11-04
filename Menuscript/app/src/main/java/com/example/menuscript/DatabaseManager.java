package com.example.menuscript;


import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Methods for accessing the firestore database
 * databaseInstance {@link FirebaseFirestore}
 * collectionReference {@link CollectionReference}
 */

public class DatabaseManager {
    private FirebaseFirestore databaseInstance;
    private CollectionReference collectionReference;
    ArrayList<StoredIngredient> ingredients = new ArrayList<>();
    Context context;

    private String descriptionFieldStr = "description";
    private String amountFieldStr = "amount";
    private String unitFieldStr = "unit";
    private String categoryFieldStr = "category";
    private String dateFieldStr = "date";
    private String locationFieldStr = "location";


    public DatabaseManager (Context _context) {
        this.databaseInstance = FirebaseFirestore.getInstance();
        context = _context;
        Log.d("DATABASE MANAGER", "DATABASE MANAGER CREATED");
    }

    /**
     *Returns an array from database of user stored ingredients.
     * @return ingredients {@link ArrayList<StoredIngredient>}
     */
    public ArrayList<StoredIngredient> getStoredIngredients () {
        //ingredients = new ArrayList<>();
        collectionReference = databaseInstance.collection("StoredIngredients");

        Log.d("DATABASE MANAGER", "called getStoredIngredients");
//        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
//                    FirebaseFirestoreException error) {
//
//                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
//                {
//                    Log.d(TAG, String.valueOf(doc.getData().get("description")));
//                    String description = (String) doc.getData().get(descriptionFieldStr);
//                    float amount = Float.parseFloat(String.valueOf(doc.getData().get(amountFieldStr)));
//                    String unit = (String) doc.getData().get(unitFieldStr);
//                    String category = (String) doc.getData().get(categoryFieldStr);
//                    String date = (String) doc.getData().get(dateFieldStr);
//                    String location = (String) doc.getData().get(locationFieldStr);
//
//                    ingredients.add(new StoredIngredient(description, amount, unit, category, date, location));
//                    if (ingredients.isEmpty()){
//                        Log.d("INGREDIENT LIST", "EMPTY");
//                    }else {
//                        Log.d("INGREDIENT LIST", "NOT EMPTY");
//                    }
//                }
//            }
//        });

        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                    public void onComplete (@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> data = document.getData();
                            ingredients.add(new StoredIngredient(
                                    (String)data.get(descriptionFieldStr),
                                    Float.parseFloat(String.valueOf(data.get(amountFieldStr))),
                                    (String)data.get(unitFieldStr),
                                    (String)data.get(categoryFieldStr),
                                    (String)data.get(dateFieldStr),
                                    (String)data.get(locationFieldStr)
                            ));
                        if (ingredients.isEmpty()){
                            Log.d("INGREDIENT LIST", "EMPTY");
                        }else {
                            Log.d("INGREDIENT LIST", "NOT EMPTY");
                        }

                        Activity activity = (Activity) context;
                            }
                    } else {
                        Log.d(TAG, "Error Getting StoredIngredients from database.");
                    }
                }
        });
        if (ingredients.isEmpty()){
            Log.d("INGREDIENT LIST", "EMPTY");
        }else{
            Log.d("INGREDIENT LIST", "NOT EMPTY");
        }
        return ingredients;
    }

    /**
     * Store ingredient into firebase collection, assumes storedIngredient has valid entries
     * @param storedIngredient
     */
    public void addStoredIngredient (StoredIngredient storedIngredient) {
        collectionReference = databaseInstance.collection("StoredIngredients");

        HashMap<String,Object> data = new HashMap<>();
        data.put(descriptionFieldStr, storedIngredient.getDescription());
        data.put(amountFieldStr, storedIngredient.getAmount());
        data.put(unitFieldStr, storedIngredient.getUnit());
        data.put(categoryFieldStr,storedIngredient.getCategory());
        data.put(dateFieldStr,storedIngredient.getDate());
        data.put(locationFieldStr,storedIngredient.getLocation());

        collectionReference.document().set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data could not be added" +e.toString());
                    }
                });
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
