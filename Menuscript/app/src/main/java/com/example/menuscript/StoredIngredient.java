package com.example.menuscript;

/**
 * StoredIngredient contains stored ingredient attributes for use in
 * recipes, shopping, and meal planning.
 * key {@link int}
 * amount {@link float}
 * date {@link String}
 * unit {@link String}
 *
 * @see Ingredient
 * @see StoredIngredients
 */
public class StoredIngredient {
    private int key;
    private float amount;
    private String date;
    private String unit;

    public StoredIngredient () {
        ;
    }

    public StoredIngredient (int key, float amount, String date, String unit) {
        this.key = key;
        this.amount = amount;
        this.date = date;
        this.unit = unit;
    }

    public int getKey () {return this.key;}
    public float getAmount () {return this.amount;}
    public String getDate () {return this.date;}
    public String getUnit () {return this.unit;}

    /**
     * Clones a StoredIngredient object and returns it.
     *
     * @return StoredIngredient object that was cloned.
     */
    public StoredIngredient clone () {
        return new StoredIngredient(this.key, this.amount, this.date, this.unit);
    }
}
