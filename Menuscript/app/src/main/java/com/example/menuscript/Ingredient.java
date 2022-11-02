package com.example.menuscript;

import java.io.Serializable;

/**
 * This class minimally defines a general ingredient for use in recipes, shopping, and meal planning.
 * @author Lane Missel
 */
public class Ingredient implements Serializable {
    /**
     * A unique integer key for each instance of Ingredient, allowing access to the specific instance of Ingredient within the {@link Ingredients Ingredients} or {@link StoredIngredients StoredIngredients} classes.
     */
    private int key;
    private String description;
    private String category;

    /**
     * Class constructor.
     */
    public Ingredient () {
    }

    /**
     * Class constructor specifying key, description, and category.
     *
     * @param key   a unique integer key to identify instance of Ingredient
     * @param description   a short description of the Ingredient
     * @param category  the category to which the Ingredient belongs
     */
    public Ingredient (int key, String description, String category) {
        this.key = key;
        this.description = description;
        this.category = category;
    }

    /**
     * Returns the {@link Integer Integer} key belonging to the Ingredient.
     * @return  the Ingredient's unique key
     */
    public int getKey () {return this.key;}

    /**
     * Returns the {@link String String} description of the Ingredient.
     * @return  the Ingredient's description.
     */
    public String getDescription () {return this.description;}

    /**
     * Returns the {@link String String} category of the Ingredient.
     * @return  the Ingredient's category.
     */
    public String getCategory () {return this.category;}
}
