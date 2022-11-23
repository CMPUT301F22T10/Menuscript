package com.example.menuscript;


import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Methods for accessing the firestore database
 * databaseInstance {@link FirebaseFirestore}
 * collectionReference {@link CollectionReference}
 * ingredients {@link ArrayList<StoredIngredient>}
 */

public class DatabaseManager {
    private FirebaseFirestore databaseInstance;
    private CollectionReference collectionReference;
    ArrayList<StoredIngredient> ingredients = new ArrayList<>();
    Context context;

    public DatabaseManager(Context _context) {
        this.databaseInstance = FirebaseFirestore.getInstance();
        context = _context;
        Log.d("DATABASE MANAGER", "DATABASE MANAGER CREATED");
    }

    /**
     * Store ingredient into firebase collection, assumes storedIngredient has valid entries
     *
     * @param storedIngredient
     */
    public void addStoredIngredient(StoredIngredient storedIngredient) {
        collectionReference = databaseInstance.collection("StoredIngredients");

        HashMap<String, Object> data = storedIngredient.asHashMap();

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
                        Log.d(TAG, "Data could not be added" + e.toString());
                    }
                });
    }

    public void deleteStoredIngredient(StoredIngredient storedIngredient) {
        collectionReference = databaseInstance.collection("StoredIngredients");

        HashMap<String, Object> data = storedIngredient.asHashMap();

        ArrayList<String> docID = new ArrayList<>();

        collectionReference
                .whereEqualTo("description", storedIngredient.getDescription())
                .whereEqualTo("category", storedIngredient.getCategory())
                .whereEqualTo("date", storedIngredient.getDate())
                .whereEqualTo("location", storedIngredient.getLocation())
                .whereEqualTo("unit", storedIngredient.getUnit())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                docID.add(document.getId());
                                Log.d("what", document.getId() + " => " + document.getData());

                                String toDelete = docID.get(0);
                                Log.d("non nonono", toDelete);
                                Log.d("mmm", docID.toString());
                                collectionReference
                                        .document(toDelete)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });

    }

    public void addRecipe(Recipe recipe) {
        ;
    }

    public void deleteRecipe(Recipe recipe) {
        ;
    }

    public void addRecipeCategory(String category) {
        DocumentReference categories = databaseInstance.collection("Options").document("Recipe Categories");

        categories.update(category, category);
    }

    public void addIngredientCategory(String category) {
        DocumentReference categories = databaseInstance.collection("Options").document("Ingredient Categories");

        categories.update(category, category);
    }
    public void addLocation(String location) {
        DocumentReference categories = databaseInstance.collection("Options").document("Locations");

        categories.update(location, location);
    }
    public void addUnit(String unit) {
        DocumentReference categories = databaseInstance.collection("Options").document("Units");

        categories.update(unit, unit);
    }
}
