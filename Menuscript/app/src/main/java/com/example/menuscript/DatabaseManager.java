package com.example.menuscript;

import java.util.ArrayList;

public class DatabaseManager {
    private Object connection;

    /**
     * Returns an array from database of user stored ingredients.
     */
    public ArrayList<StoredIngredient> getStoredIngredients () {
        ArrayList<StoredIngredient> ingredients = new ArrayList<StoredIngredient>();
        ingredients.add(new StoredIngredient("Carrots", 2, "pounds", "vegetable", "10/11/2022", "Fridge"));
        ingredients.add(new StoredIngredient("Apples", 1, "pounds", "fruit", "10/11/2022", "Fridge"));
        ingredients.add(new StoredIngredient("Tomato Soup", 2, "litres", "canned goods", "10/11/2027", "pantry"));
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
}
