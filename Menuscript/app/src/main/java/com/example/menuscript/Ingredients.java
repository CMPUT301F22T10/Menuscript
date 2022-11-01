package com.example.menuscript;
import java.security.KeyException;
import java.util.ArrayList;

/**
 * @author Lane Missel
 */
public class Ingredients {
    private ArrayList<Ingredient> values;
    private Options categories;

    public Ingredients () {
        this.values = new ArrayList<Ingredient>();
        this.categories = new Options();
    }

    /**
     * Returns index of ingredient with given key.
     *
     * @param key
     * @return int
     */
    private int find(int key) {
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i).getKey() == key) {
                return i;
            }
        }
        return -1;
    }

    public Ingredient get(int key) throws KeyException {

        int index = find(key);

        if (index < 0) {
            throw new KeyException(String.format("An Ingredient with key %d does not exist", key));
        }

        Ingredient ingredient = values.get(index);
        return new Ingredient(ingredient.getKey(), ingredient.getDescription(), ingredient.getCategory());
    }

    public void add (String description, String category) throws IllegalArgumentException {
        int max = -1;

        // check category
        if (!categories.has(category)) {
            throw new IllegalArgumentException("Category does not exist.");
        }

        // find key
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i).getKey() > max) {
                max = values.get(i).getKey();
            }
        }

        max++;

        values.add(new Ingredient(max, description, category));
    }

    public void add (int key, String description, String category) throws KeyException {
        assert(categories.has(category));

        Ingredient ingredient;

        // Ensure key is valid
        int index = find(key);

        if (index < 0) {
            throw new KeyException();
        }

        values.add(new Ingredient(key, description, category));
    }

    public void remove (int key) throws KeyException {
        int index = find(key);

        if (index < 0) {
            throw new KeyException();
        }

        values.remove(index);
    }

    public boolean has (int key) {
        return find(key) > -1;
    }

    public void addCategory (String value) {
        categories.add(value);
    }

    public void removeCategory (String value) {
        categories.remove(value);
    }

    public ArrayList<String> getCategories () {
        return new ArrayList<String>();
    }
}
