package com.example.menuscript;

public class StoredIngredient extends Ingredient {
    private String date;
    private String location;

    public StoredIngredient (String description, float amount, String unit, String  category, String date, String location) {
        super(description, amount, unit, category);
        this.date = date;
        this.location = location;
    }

    public String getDate () {return this.date;}
    public String getLocation () {return this.location;}
}
