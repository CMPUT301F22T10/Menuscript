package com.example.menuscript;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class DatabaseManagerUnitTest {
    @Test
    public void testConstructor () {
        DatabaseManager db = new DatabaseManager();
    }

    @Test
    public void testGetStoredIngredient () {
        DatabaseManager db = new DatabaseManager();
        ArrayList<StoredIngredient> ingredients = db.getStoredIngredients();
        StoredIngredient x, y;
        x = ingredients.get(0);
        y = new StoredIngredient("Carrot",
                2,
                "pounds",
                "vegetable",
                "10/11/2022",
                "fridge");
        assertTrue(x == y);
    }
}
