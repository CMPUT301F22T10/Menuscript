package com.example.menuscript;

import java.util.Date;

public class StoredTestIngredient extends TestIngredient {
    private Date bestBefore;
    private String location;

    public StoredTestIngredient(String description, String category) {
        super(description, category);
    }

    public StoredTestIngredient(String description, String category, Integer amount, String unit) {
        super(description, category, amount, unit);
    }

    public StoredTestIngredient(String description, String category, Integer amount, String unit, Date bestBefore, String location) {
        super(description, category, amount, unit);
        this.bestBefore = bestBefore;
        this.location = location;
    }

    public Date getBestBefore() {
        return bestBefore;
    }

    public String getLocation() {
        return location;
    }

    public void setBestBefore(Date bestBefore) {
        this.bestBefore = bestBefore;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
