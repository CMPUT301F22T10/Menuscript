package com.example.menuscript;

import java.util.Date;

public class TestIngredient {
    private String description;
    private String category;
    private Integer amount;
    private String unit;

    public TestIngredient(String description, String category) {
        this.description = description;
        this.category = category;
        amount = 0;
        unit = null;
    }

    public TestIngredient(String description, String category, Integer amount, String unit) {
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.unit = unit;
    }


    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

