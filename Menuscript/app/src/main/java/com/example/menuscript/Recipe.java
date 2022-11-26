package com.example.menuscript;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

/**
 * This class creates/defines a Recipe object with eight variables:
 * title {@link String}
 * time {@link int}
 * servings {@link float}
 * category {@link String}
 * comments {@link String}
 * image {@link Bitmap}
 * ingredients {@link ArrayList<Ingredient>}
 *
 * @author Dylan
 */
public class Recipe {
    private String title;
    private int time;
    private float servings;
    private String category;
    private String comments;
    private byte[] image;
    private ArrayList<Ingredient> ingredients;

    /**
     * This is a constructor to create a Recipe object.
     *
     * @param title Stores the name of the recipe {@link String}
     * @param time The time needed to prepare the recipe {@link int}
     * @param servings Amount of servings that the recipe produces {@link float}
     * @param category A category to describe the recipe's type {@link String}
     * @param comments Notes/Comments about the recipe {@link String}
     * @param image A byte representation of a recipe's image {@link byte[]}
     * @param ingredients A list of ingredients that the recipe uses {@link ArrayList<Ingredient>}
     */

    public Recipe(String title, int time, float servings, String category, String comments, byte[] image, ArrayList<Ingredient> ingredients) {
        this.title = title;
        this.time = time;
        this.servings = servings;
        this.category = category;
        this.comments = comments;
        this.image = image;
        this.ingredients = ingredients;
    }

    public String getTitle() {
        return title;
    }

    public int getTime() {
        return time;
    }

    public float getServings() {
        return servings;
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

    public byte[] getImage() {
        return image;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<HashMap<String,String>> getHashedIngredients() {
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

        for(Ingredient ingredient : this.ingredients) {
            HashMap<String,String> hm = new HashMap<String,String>();
            hm.put("amount", String.valueOf(ingredient.getAmount()));
            hm.put("category", ingredient.getCategory());
            hm.put("description", ingredient.getDescription());
            hm.put("unit", ingredient.getUnit());
            list.add(hm);
        }

        return list;
    }

    public String getEncodedImage(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Log.d("myTag", "This is it: " + Base64.getEncoder().encodeToString(this.image));
            return Base64.getEncoder().encodeToString(this.image);
        }
        else{
            return null;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setServings(float servings) {
        this.servings = servings;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public HashMap<String, Object> asHashMap () {
        HashMap<String, Object> data = new HashMap<>();
        data.put("title", this.title);
        data.put("time", this.time); //its a number in the database? maybe shouldn't string valueof it?
        data.put("servings", String.valueOf(this.servings));
        data.put("category", this.category);
        data.put("comments", this.comments);
        data.put("image", getEncodedImage());
        ArrayList<HashMap<String,String>> list = getHashedIngredients();
        data.put("ingredients",list);
        return data;
    }

}
