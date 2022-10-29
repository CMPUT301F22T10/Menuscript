package com.example.menuscript;

import java.util.ArrayList;

public class Ingredients {
    private ArrayList<Ingredient> values;
    private Options categories;

    public Ingredients () {
        ;
    }

    public Ingredient get(int key) {
        return new Ingredient();
    }

    public void add (String description, String category) {
        ;
    }

    public void add (int key, String description, String category) {
        ;
    }

    public void remove (int key) {
        ;
    }

    public boolean has (int key) {
        return true;
    }

    public void addCategory (String value) {
        ;
    }

    public void removeCategory (String value) {
        ;
    }

    public ArrayList<String> getCategories () {
        return new ArrayList<String>();
    }
}
