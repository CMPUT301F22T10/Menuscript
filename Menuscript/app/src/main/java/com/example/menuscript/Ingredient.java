package com.example.menuscript;

import java.io.Serializable;

/**
 * This class minimally defines a general ingredient for use in recipes, shopping, and meal planning.
 * @author Lane Missel
 */
public class Ingredient implements Serializable {
    protected String description;
    protected float amount;
    protected String unit;
    protected String category;

    /**
     * Class constructor.
     */
    public Ingredient () {
        this.description = "";
        this.amount = 0;
        this.unit = null;
        this.category = null;
    }

    /**
     * Class constructor specifying key, description, and category.
     *
     * @param description   a short description of the Ingredient
     * @param category  the category to which the Ingredient belongs
     */
    public Ingredient (String description, float amount, String unit, String category) {
        assert(amount >= 0);

        this.description = description;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }

    /**
     * Returns the {@link String String} description of the Ingredient.
     * @return  the Ingredient's description.
     */
    public String getDescription () {return this.description;}

    public float getAmount () { return this.amount;}

    public String getUnit () {return this.unit;}

    /**
     * Returns the {@link String String} category of the Ingredient.
     * @return  the Ingredient's category.
     */
    public String getCategory () {return this.category;}
}
