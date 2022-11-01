package com.example.menuscript;

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

    public StoredIngredient clone () {
        return new StoredIngredient(this.key, this.amount, this.date, this.unit);
    }
}
