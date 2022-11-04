package com.example.menuscript;
import java.security.KeyException;
import java.util.ArrayList;

/**
 * Ingredients is a class contains an ArrayList of Ingredient objects
 * and Options objects which contain the category of the Ingredient.
 * values {@link ArrayList<Ingredient>}
 * categories {@link Options}
 *
 * @author Lane Missel
 * @see Ingredient
 * @see Options
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
     * @param key           the unique key of an Ingredient object.
     * @return int          key if Ingredient object with the same key exists;
     *                      -1 otherwise.
     */
    private int find(int key) {
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i).getKey() == key) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns an Ingredient object if it matches the given key passed in.
     * Throws KeyException if Ingredient with the given key is not found.
     * index {@link int}
     *
     * @param key                 the unique key of an Ingredient object.
     * @return Ingredient         if an Ingredient object exists with the
     *                            parameter passed in the get() method.
     * @throws KeyException       if the key is not found.
     * @see Ingredient
     */
    public Ingredient get(int key) throws KeyException {

        int index = find(key);

        if (index < 0) {
            throw new KeyException(String.format("An Ingredient with key %d does not exist", key));
        }

        Ingredient ingredient = values.get(index);
        return new Ingredient(ingredient.getKey(), ingredient.getDescription(), ingredient.getCategory());
    }

    /**
     * Adds an Ingredient object into an ArrayList if the category
     * parameter matches preexisting categories.
     *
     * @param description               description of the Ingredient object.
     * @param category                  category of the Ingredient object.
     * @throws IllegalArgumentException if category is not an existing option.
     * @see Ingredient
     */
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

    /**
     * Adds an Ingredient object into an ArrayList if the category
     * parameter matches preexisting categories. The Ingredient object
     * is instantiated with the given key as long as the key is unique.
     * index {@link int}
     *
     * @param key                       the unique key of an Ingredient object.
     * @param description               description of the Ingredient object.
     * @param category                  category of the Ingredient object.
     * @throws KeyException             if category is not an existing option
     * @see Ingredient
     */
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

    /**
     * Removes an Ingredient with the given corresponding key.
     * index {@link int}
     *
     * @param key               the unique key of an Ingredient object.
     * @throws KeyException
     * @see Ingredient
     */
    public void remove (int key) throws KeyException {
        int index = find(key);

        if (index < 0) {
            throw new KeyException();
        }

        values.remove(index);
    }

    /**
     * Checks if the given key exists.
     *
     * @param key       the unique key of an Ingredient object.
     * @return          1 if Ingredient with the given key exists;
     *                 -1 otherwise.
     */
    public boolean has (int key) {
        return find(key) > -1;
    }

    /**
     * Adds a category with the string given.
     *
     * @param value     the name of the category.
     */
    public void addCategory (String value) {
        categories.add(value);
    }

    /**
     * Removes a category with the string given.
     *
     * @param value     the name of the category.
     */
    public void removeCategory (String value) {
        categories.remove(value);
    }

    /**
     * Returns an ArrayList containing all categories stored.
     *
     * @return ArrayList<String>        an array of categories.
     */
    public ArrayList<String> getCategories () {
        return new ArrayList<String>();
    }
}
