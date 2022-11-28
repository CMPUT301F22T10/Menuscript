package com.example.menuscript;


import static android.content.ContentValues.TAG;
import static android.provider.MediaStore.MediaColumns.DOCUMENT_ID;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
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
import java.util.Base64;
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

    private String descriptionFieldStr = "description";
    private String amountFieldStr = "amount";
    private String unitFieldStr = "unit";
    private String categoryFieldStr = "category";
    private String dateFieldStr = "date";
    private String locationFieldStr = "location";


    public DatabaseManager(Context _context) {
        this.databaseInstance = FirebaseFirestore.getInstance();
        context = _context;
        Log.d("DATABASE MANAGER", "DATABASE MANAGER CREATED");
    }

    /**
     * Returns an array from database of user stored ingredients.
     *
     * @return ingredients {@link ArrayList<StoredIngredient>}
     */
    public ArrayList<StoredIngredient> getStoredIngredients() {
        //ingredients = new ArrayList<>();
        collectionReference = databaseInstance.collection("StoredIngredients");

        Log.d("DATABASE MANAGER", "called getStoredIngredients");

        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                ingredients.add(new StoredIngredient(
                                        (String) data.get(descriptionFieldStr),
                                        Float.parseFloat(String.valueOf(data.get(amountFieldStr))),
                                        (String) data.get(unitFieldStr),
                                        (String) data.get(categoryFieldStr),
                                        (String) data.get(dateFieldStr),
                                        (String) data.get(locationFieldStr)
                                ));
                                if (ingredients.isEmpty()) {
                                    Log.d("INGREDIENT LIST", "EMPTY");
                                } else {
                                    Log.d("INGREDIENT LIST", "NOT EMPTY");
                                }

                                Activity activity = (Activity) context;
                            }
                        } else {
                            Log.d(TAG, "Error Getting StoredIngredients from database.");
                        }
                    }
                });
        if (ingredients.isEmpty()) {
            Log.d("INGREDIENT LIST", "EMPTY");
        } else {
            Log.d("INGREDIENT LIST", "NOT EMPTY");
        }
        return ingredients;
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

     /**
     * Add ingredient to shopping list
     * @param ingredient
     */
    public void addShoppingItem(Ingredient ingredient){
        collectionReference = databaseInstance.collection("ShoppingList");

        HashMap<String, Object> data = ingredient.asHashMap();

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
        CollectionReference  mealPlanIngredientsCollectionReference = databaseInstance.collection("MealPlanIngredients");

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
                                //delete ingredient from meal plan
                                mealPlanIngredientsCollectionReference
                                        .whereEqualTo(DOCUMENT_ID, toDelete)
                                        .get()
                                        .addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()){
                                                mealPlanIngredientsCollectionReference.document(toDelete).delete();
                                            }
                                        });

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

    public void editIngredient(StoredIngredient original, StoredIngredient replacement) {
        collectionReference = databaseInstance.collection("StoredIngredients");
        CollectionReference  mealPlanIngredientsCollectionReference = databaseInstance.collection("MealPlanIngredients");

        ArrayList<String> docID = new ArrayList<>();

        collectionReference
                .whereEqualTo("description", original.getDescription())
                .whereEqualTo("category", original.getCategory())
                .whereEqualTo("date", original.getDate())
                .whereEqualTo("location", original.getLocation())
                .whereEqualTo("unit", original.getUnit())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                docID.add(document.getId());
                                String toEdit = docID.get(0);
                                Log.d("CHECKLIST QUERY FOUND", toEdit);

                                //update meal plan as well if description or unit is changed
                                mealPlanIngredientsCollectionReference
                                        .whereEqualTo(DOCUMENT_ID, toEdit )
                                        .get()
                                        .addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()){
                                                mealPlanIngredientsCollectionReference.document(toEdit).update("description", replacement.getDescription());
                                                mealPlanIngredientsCollectionReference.document(toEdit).update("unit", replacement.getUnit());
                                            }
                                        });

                                collectionReference
                                        .document(toEdit)
                                        .set(replacement)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d(TAG, "DocumentSnapshot successfully edited!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, "Error editing document", e);
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
        collectionReference = databaseInstance.collection("Recipes");

        HashMap<String,Object> data = recipe.asHashMap();

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

    public void deleteRecipe(Recipe recipe) {
        collectionReference = databaseInstance.collection("Recipes");
        CollectionReference mealPlanRecipesCollectionReference = databaseInstance.collection("MealPlanRecipes");

        //HashMap<String,Object> data = recipe.asHashMap();

        ArrayList<String> docID = new ArrayList<>();

            collectionReference
                    .whereEqualTo("title",recipe.getTitle())
                    .whereEqualTo("time",recipe.getTime())
                    .whereEqualTo("servings",String.valueOf(recipe.getServings()))
                    .whereEqualTo("category",recipe.getCategory())
                    .whereEqualTo("comments",recipe.getComments())
                    //.whereEqualTo("image", recipe.getEncodedImage())
                    .whereEqualTo("ingredients",recipe.getHashedIngredients())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    docID.add(document.getId());


                                    Log.d("what", document.getId() + " => " + document.getData());

                                    String toDelete = docID.get(0);

                                    //delete recipe from meal plan
                                    mealPlanRecipesCollectionReference
                                            .whereEqualTo(DOCUMENT_ID, toDelete)
                                            .get()
                                            .addOnCompleteListener(task2 -> {
                                                if (task2.isSuccessful()){
                                                    mealPlanRecipesCollectionReference.document(toDelete).delete();
                                                }
                                            });

                                    Log.d("myTag",toDelete);
                                    Log.d("myTag",docID.toString());
                                    collectionReference
                                            .document(toDelete)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d("myTag", "DocumentSnapshot successfully deleted");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("myTag","Error deleting document",e);
                                                }
                                            });
                                }
                            }
                            else {
                                Log.d("myTag", "Error getting documents: ", task.getException());
                            }
                        }
                    });


    }

    public void editRecipe(Recipe recipe, Recipe edittedRecipe) {
        collectionReference = databaseInstance.collection("Recipes");
        CollectionReference mealPlanRecipesCollectionReference = databaseInstance.collection("MealPlanRecipes");
        //HashMap<String,Object> data = recipe.asHashMap();

        ArrayList<String> docID = new ArrayList<>();

        collectionReference
                .whereEqualTo("title",recipe.getTitle())
                .whereEqualTo("time",recipe.getTime())
                .whereEqualTo("servings",String.valueOf(recipe.getServings()))
                .whereEqualTo("category",recipe.getCategory())
                .whereEqualTo("comments",recipe.getComments())
                //.whereEqualTo("image", recipe.getEncodedImage())
                .whereEqualTo("ingredients",recipe.getHashedIngredients())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                docID.add(document.getId());

                                Log.d("myTag", document.getId() +"=>"+document.getData());
                                String toEdit = docID.get(0);

                                //update meal plan as well if description changed
                                mealPlanRecipesCollectionReference
                                        .whereEqualTo(DOCUMENT_ID, toEdit  )
                                        .get()
                                        .addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()){
                                                mealPlanRecipesCollectionReference.document(toEdit ).update("title", edittedRecipe.getTitle());
                                            }
                                        });

                                Log.d("myTag",toEdit);
                                Log.d("myTag",docID.toString());
                                collectionReference
                                        .document(toEdit)
                                        .update("title",edittedRecipe.getTitle(),"time",edittedRecipe.getTime(),
                                                "servings",String.valueOf(edittedRecipe.getServings()),"category",edittedRecipe.getCategory(),
                                                "comments",edittedRecipe.getComments(),"image",edittedRecipe.getEncodedImage(),"ingredients",
                                                edittedRecipe.getHashedIngredients())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("myTag", "DocumentSnapshot successfully editted");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("myTag","Error editting document",e);
                                            }
                                        });
                            }
                        }
                        else {
                            Log.d("myTag", "Error getting documents: ", task.getException());
                        }
                    }
                });


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
