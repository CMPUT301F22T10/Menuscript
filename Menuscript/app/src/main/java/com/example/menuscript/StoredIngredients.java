package com.example.menuscript;

import java.security.KeyException;
import java.util.ArrayList;

public class StoredIngredients {
    private ArrayList<StoredIngredient> storage;

    public StoredIngredients () {
        ;
    }

    public StoredIngredients (ArrayList<StoredIngredient> ingredients) {
        for (int i = 0; i < ingredients.size(); i++) {
            StoredIngredient ingredient = ingredients.get(i);
            add(ingredient.clone());
        }
    }

    public void add(int key, float amount, String unit) {
        storage.add(new StoredIngredient(key, amount, null, unit));
    }

    public void add(int key, float amount, String date, String unit) {
        storage.add(new StoredIngredient(key, amount, date, unit));
    }

    public void add(StoredIngredient ingredient) {
        storage.add(ingredient.clone());
    }

    public StoredIngredient get(int key) throws KeyException {
        for (int i = 0; i < storage.size(); i++) {
            StoredIngredient ingredient = storage.get(i);
            if (ingredient.getKey() == key) {
                return ingredient.clone();
            }
        }

        throw new KeyException("This key does not exist.");
    }

    private int getIndex(int key) {
        for (int i = 0; i < storage.size(); i++) {
            if (key == storage.get(i).getKey()) {
                return i;
            }
        }

        return -1;
    }

    public void use (int key, float amount, String unit) throws KeyException, IllegalArgumentException {
        int index = getIndex(key);
        StoredIngredient ingredient = storage.get(index);

        float new_amount = ingredient.getAmount() - amount;

        if (new_amount < 0) {
            throw new IllegalArgumentException("Not enough ingredient to remove.");
        }

        storage.set(index, new StoredIngredient(key, new_amount, ingredient.getDate(), ingredient.getUnit()));
    }

    public boolean has(int key) {
        return getIndex(key) >= 0;
    }

}
