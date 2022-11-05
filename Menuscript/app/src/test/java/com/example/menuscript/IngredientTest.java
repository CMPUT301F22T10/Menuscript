package com.example.menuscript;

import org.junit.Before;
import org.junit.Test;

public class IngredientTest {
    Ingredient ingredient;

    @Test
    public void testInit () {
        ingredient = new Ingredient();
        assert(ingredient.getDescription() == "");
        assert(ingredient.getAmount() == 0);
        assert(ingredient.getUnit() == null);
        assert(ingredient.getCategory() == null);
    }

    @Test
    public void testInitArguments () {
        ingredient = new Ingredient("My Ingredient", (float)20, "units", "this");
        assert(ingredient.getDescription() == "My Ingredient");
        assert(ingredient.getAmount() == (float)20);
        assert(ingredient.getUnit() == "units");
        assert(ingredient.getCategory() == "this");

    }

    @Test(expected = AssertionError.class)
    public void testNegative () {
        ingredient = new Ingredient("A", (float)-23.4, "U", "C");
    }
}
