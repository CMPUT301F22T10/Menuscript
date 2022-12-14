package com.example.menuscript;

import java.util.HashMap;

/**
 * StoredIngredient contains stored ingredient attributes for use in
 * recipes, shopping, and meal planning.
 * date {@link String}
 * location {@link String}
 *
 * @see Ingredient
 */
public class StoredIngredient extends Ingredient {
    private String date;
    private String location;

    public StoredIngredient() {}

    public StoredIngredient (String description, float amount, String unit, String  category, String date, String location) {
        super(description, amount, unit, category);
        this.date = date;
        this.location = location;
    }

    public String getDate () {return this.date;}
    public String getLocation () {return this.location;}

    @Override
    public float getAmount() {
        return super.getAmount();
    }

    @Override
    public String getCategory() {
        return super.getCategory();
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public String getUnit() {
        return super.getUnit();
    }

    /**
     * Method to return a hashmap filled with data of the ingredient with
     * the keys as what values represent
     * data {@link HashMap<String, Object>}
     *
     * @return  data    a hashmap containing all the details of ingredients
     *                  associated with right keys.
     */
    public HashMap<String, Object> asHashMap () {
        HashMap<String, Object> data  = new HashMap<>();
        data.put("description", this.description);
        data.put("amount", this.amount);
        data.put("unit", this.unit);
        data.put("category", this.category);
        data.put("date", this.date);
        data.put("location", this.location);
        return data;
    }
}
