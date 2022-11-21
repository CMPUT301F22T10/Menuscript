package com.example.menuscript;

import java.util.ArrayList;

public class MealPlan {
    private ArrayList<Recipe> recipes;
    private ArrayList<Ingredient> ingredients;

    public MealPlan () {

        this.recipes = new ArrayList<>();
        this.ingredients = new ArrayList<>();
    }

    public void addRecipe (Recipe recipe) {
        this.recipes.add(recipe);
    }

    public void deleteRecipe (Recipe recipe) {

        this.recipes.remove(recipe);
    }

    public ArrayList<Recipe> getRecipes () {
        return this.recipes;
    }

    public void addIngredient (Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public void deleteIngredient (Ingredient ingredient) {

        this.ingredients.remove(ingredient);
    }

    public ArrayList<Ingredient> getIngredients () {
        return this.ingredients;
    }
}
