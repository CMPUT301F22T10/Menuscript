package com.example.menuscript;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

public class RecipeTest {
    Recipe recipe;
    ArrayList<Ingredient> ingredients;
    Ingredient ingredient;


    @Before
    public void before () {
        //ingredients.add(new Ingredient("flour", (float)2, "cups", "grain"));
        //ingredients.add(new Ingredient("butter", (float)0.5, "pound", "dairy"));

        recipe = new Recipe("Recipe",
                244, 4, "breaky",
                "this is really good",
                null, ingredients);
    }

    @Test
    public void testGetTitle () {
        assert(recipe.getTitle() == "Recipe");
    }

    @Test
    public void testGetTime () {
        assert(recipe.getTime() == 244);
    }

    @Test
    public void testGetServings () {
        assert(recipe.getServings() == 4);
    }

    @Test
    public void testGetCategory () {
        assert(recipe.getCategory() == "breaky");
    }

    @Test
    public void testGetComments () {
        assert(recipe.getComments() == "this is really good");
    }

    @Test
    @Ignore
    public void testGetIngredients () {
        ArrayList<Ingredient> x = recipe.getIngredients();
        assert(x.size() == 0);
        //assert(x.get(0).getDescription() == "flour");
    }

    @Test
    public void setTitle () {
        recipe.setTitle("newTitle");
        assert(recipe.getTitle() == "newTitle");
    }

    @Test
    public void setComments () {
        recipe.setComments("This isn't so good anymore");
        assert(recipe.getComments() == "This isn't so good anymore");
    }
}
