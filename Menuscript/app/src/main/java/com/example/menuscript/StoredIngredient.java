package com.example.menuscript;

import java.util.HashMap;

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

    @Override
    public boolean equals (Object x) {
        StoredIngredient other = (StoredIngredient) x;
        if (this.description == other.description &&
            this.amount == other.amount &&
                this.unit == other.unit &&
                this.category == other.category &&
                this.date == other.date &&
                this.location == other.location
        ) {
            return true;
        } else {
            return false;
        }
    };
}
