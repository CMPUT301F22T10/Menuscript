package com.example.menuscript;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class DatabaseManagerTest {
    private DatabaseManager db;

    @Before
    public void setup () {
        this.db = new DatabaseManager(null);
    }

    @Test
    @Ignore
    public void addIngredientToMealPlanTest () {
        Ingredient ingredient = new Ingredient("Test MealPlan Ingredient", 2, "units", "category");
        this.db.addIngredientToMealPlan(ingredient);
    }
}
