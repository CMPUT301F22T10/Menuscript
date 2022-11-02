package com.example.menuscript;

import java.io.Serializable;

/**
 * @author Lane Missel
 */
public class Ingredient implements Serializable {
    private int key;
    private String description;
    private String category;

    public Ingredient () {
        ;
    }

    public Ingredient (int key, String description, String category) {
        this.key = key;
        this.description = description;
        this.category = category;
    }

    public int getKey () {return this.key;}

    public String getDescription () {return this.description;}

    public String getCategory () {return this.category;}
}
