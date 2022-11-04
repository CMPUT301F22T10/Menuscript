package com.example.menuscript;

import java.security.KeyException;
import java.util.ArrayList;

/**
 * StoredIngredients is a class contains an ArrayList of StoredIngredient
 * objects.
 * storage {@link ArrayList<StoredIngredient>}
 *
 * @see StoredIngredient
 */
public class StoredIngredients {
    private ArrayList<StoredIngredient> storage;

    public StoredIngredients () {
        ;
    }

    /**
     * Constructs a StoredIngredients object which clones the inventory of
     * StoredIngredient objects to the storage ArrayList.
     *
     * @param ingredients an ArrayList containing StoredIngredient objects.
     */
    public StoredIngredients (ArrayList<StoredIngredient> ingredients) {
        for (int i = 0; i < ingredients.size(); i++) {
            StoredIngredient ingredient = ingredients.get(i);
            add(ingredient.clone());
        }
    }

    /**
     * Adds a newly created StoredIngredient object into storage without date.
     *
     * @param key       unique key tied to the (Stored)Ingredient.
     * @param amount    quantity of the StoredIngredient.
     * @param unit      units of measurement.
     */
    public void add(int key, float amount, String unit) {
        storage.add(new StoredIngredient(key, amount, null, unit));
    }

    /**
     * Adds a newly created StoredIngredient object into storage.
     *
     * @param key       unique key tied to the (Stored)Ingredient.
     * @param amount    quantity of the StoredIngredient.
     * @param date      the best before date.
     * @param unit      units of measurement.
     */
    public void add(int key, float amount, String date, String unit) {
        storage.add(new StoredIngredient(key, amount, date, unit));
    }

    /**
     * Adds a newly cloned StoredIngredient object into storage.
     *
     * @param ingredient
     */
    public void add(StoredIngredient ingredient) {
        storage.add(ingredient.clone());
    }

    /**
     *
     *
     * @param key                   the unique key of an (Stored)Ingredient.
     * @return StoredIngredient     returns a clone of the StoredIngredient
     *                              if the key given matches the key of
     *                              a StoredIngredient key.
     * @throws KeyException         if the given key does not exist.
     */
    public StoredIngredient get(int key) throws KeyException {
        for (int i = 0; i < storage.size(); i++) {
            StoredIngredient ingredient = storage.get(i);
            if (ingredient.getKey() == key) {
                return ingredient.clone();
            }
        }

        throw new KeyException("This key does not exist.");
    }

    /**
     * Returns the position of the object with the same key given.
     *
     * @param key   the unique key of a (Stored)Ingredient.
     * @return      key if there exists an object with the key given;
     *              -1 otherwise.
     */
    private int getIndex(int key) {
        for (int i = 0; i < storage.size(); i++) {
            if (key == storage.get(i).getKey()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * This method removes an ingredient with the same given key by the
     * amount passed in. Any changes to the Ingredient are updated in
     * storage. Negative amounts of Ingredients raise exceptions.
     * index {@link int}
     *
     * @param key                           the unique key of a (Stored)Ingredient.
     * @param amount                        quantity of the StoredIngredient.
     * @param unit                          units of measurement.
     * @throws KeyException                 if key given matches no object
     * @throws IllegalArgumentException     if the amount used is higher than storage amount.
     */
    public void use (int key, float amount, String unit) throws KeyException, IllegalArgumentException {
        int index = getIndex(key);
        StoredIngredient ingredient = storage.get(index);

        float new_amount = ingredient.getAmount() - amount;

        if (new_amount < 0) {
            throw new IllegalArgumentException("Not enough ingredient to remove.");
        }

        storage.set(index, new StoredIngredient(key, new_amount, ingredient.getDate(), ingredient.getUnit()));
    }

    /**
     * This method returns a key if the same key exists in storage.
     *
     * @param key       the unique key of a (Stored)Ingredient
     * @return          key if it matches an object's key in storage;
     *                  -1 otherwise.
     */
    public boolean has(int key) {
        return getIndex(key) >= 0;
    }

}
