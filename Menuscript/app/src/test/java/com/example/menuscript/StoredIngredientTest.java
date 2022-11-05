package com.example.menuscript;

import org.junit.Test;

import java.util.HashMap;

public class StoredIngredientTest {
    StoredIngredient ingredient;

    @Test
    public void testInitArgs () {
        ingredient = new StoredIngredient("ING", 2, "ml", "food", "01/01/1000", "here");
        assert("ING" == ingredient.getDescription());
        assert(2 == ingredient.getAmount());
        assert("ml" == ingredient.getUnit());
        assert("food" == ingredient.getCategory());
        assert("01/01/1000" == ingredient.getDate());
        assert("here" == ingredient.getLocation());
    }

    @Test(expected = AssertionError.class)
    public void testInitIllegalArgs () {
        ingredient = new StoredIngredient("A", -2, "a", "b", "01/01/2000", "counter");
    }

    @Test
    public void testAsHashMap () {
        ingredient = new StoredIngredient("ABC", 1, "a", "c", "01/01/2030", "l");
        HashMap<String, Object> hashed = ingredient.asHashMap();
        assert((String)hashed.get("description") == "ABC");
        assert((float)hashed.get("amount") == 1);
        assert((String)hashed.get("unit") == "a");
        assert((String)hashed.get("category") == "c");
        assert((String)hashed.get("date") == "01/01/2030");
        assert((String)hashed.get("location") == "l");
    }
}
