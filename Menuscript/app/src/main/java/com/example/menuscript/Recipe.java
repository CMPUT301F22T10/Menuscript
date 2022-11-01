package com.example.menuscript;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * This class creates/defines a Recipe object with eight variables:
 * Key {@link int}
 * Title {@link String}
 * Time {@link int}
 * Servings {@link float}
 * Category {@link String}
 * Comments {@link String}
 * Image {@link Bitmap}
 * Ingredients {@link ArrayList<Ingredient>}
 * @author Dylan
 *
 *
 */
public class Recipe {
    private int key;
    private String title;
    private int time;
    private float servings;
    private String category;
    private String comments;
    //private Bitmap image;
    private ArrayList<Ingredient> ingredients;


    /**
     * This is a constructor to create a City object. (can write packagename.classname#city)
     * @param key To be used for server functionality/keeping track of recipes {@link int}
     * @param title Stores the name of the recipe {@link String}
     * @param time The time needed to prepare the recipe {@link int}
     * @param servings Amount of servings that the recipe produces {@link float}
     * @param category A category to describe the recipe's type {@link String}
     * @param comments Notes/Comments about the recipe {@link String}
     *
     * @param ingredients A list of ingredients that the recipe uses {@link ArrayList<Ingredient>}
     */

    public Recipe(int key, String title, int time, float servings, String category, String comments, ArrayList<Ingredient> ingredients) {
        //ADD BITMAP BACK TO CONSTRUCTOR
        this.key = key;
        this.title = title;
        this.time = time;
        this.servings = servings;
        this.category = category;
        this.comments = comments;
        //this.image = image;
        this.ingredients = ingredients;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public float getServings() {
        return servings;
    }

    public void setServings(float servings) {
        this.servings = servings;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    /*
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }*/

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
