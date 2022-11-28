package com.example.menuscript;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Test class for RecipeListActivity. All the UI tests are written here. Robotium test framework is
 used
 * @author Wanlin, Dylan
 */
public class RecipeListActivityTest {
    private Solo solo;
    Recipe recipe;
    ArrayList<Ingredient> ingredients;
    Ingredient ingredient;
    @Rule
    public ActivityTestRule<RecipeListActivity> rule =
            new ActivityTestRule<RecipeListActivity>(RecipeListActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        recipe = new Recipe("TestRecipe",
                244, 5, "Uncategorized",
                "this is really good",
                null, ingredients);
    }

    /**
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }


    /**
     * Check whether activity correctly switched
     */

    @Test
    public void checkClickShowActivity(){
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);
        solo.clickInList(1);
        solo.assertCurrentActivity("Wrong Activity", ViewRecipeActivity.class);
    }

    /**
     * Checks if activity correctly switches for adding.
     */
    @Test
    public void checkClickAddRecipe(){
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);
        //solo.clickOnButton(R.id.add_item_button);
        solo.clickOnView(solo.getView(R.id.add_item_button));
        solo.assertCurrentActivity("Wrong Activity", AddRecipeActivity.class);
    }

    /**
     * Checks if a Recipe can be added, if it can be viewed in the RecipeList, if it can be edited, and if it can be deleted.
     */
    @Test
    public void checkTestAddEditDeleteRecipe(){
        //Add/Edit/Delete are all in one test to minimize pollution of database with test recipes
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);
        //solo.clickOnButton(R.id.add_item_button);
        solo.clickOnView(solo.getView(R.id.add_item_button));
        solo.assertCurrentActivity("Wrong Activity", AddRecipeActivity.class);
        solo.enterText((EditText) solo.getView(R.id.recipeAddTitle),"TestRecipe");
        solo.enterText((EditText) solo.getView(R.id.recipeAddTime),"244");
        solo.enterText((EditText) solo.getView(R.id.recipeAddServings),"5");
        solo.clickOnView(solo.getView(R.id.recipeAddCategory));
        solo.scrollToTop();
        solo.sleep(2000);
        solo.clickOnText("lunch");
        solo.sleep(1500);
        solo.clickInList(1);
        solo.enterText((EditText) solo.getView(R.id.recipeAddComments),"this is really good");
        solo.sleep(1500);
        solo.clickOnView(solo.getView(R.id.recipeAddConfirm));
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);
        solo.waitForText("TestRecipe",1,500);
        solo.clickOnText("TestRecipe"); //also serves to check if Recipe was successfully added
        solo.assertCurrentActivity("Wrong Activity", ViewRecipeActivity.class);
        //checking if everything was added correctly
        assertTrue(solo.waitForText("TestRecipe",1,500));
        assertTrue(solo.waitForText("244",1,500));
        assertTrue(solo.waitForText("5",1,500));
        assertTrue(solo.waitForText("this is really good",1,500));
        assertTrue(solo.waitForText("lunch",1,500));

        ViewRecipeActivity activity = (ViewRecipeActivity) solo.getCurrentActivity();
        final ArrayList<Ingredient> ingredientList = activity.ingredientList;
        final ArrayList<Ingredient> addedIngredientList = activity.addedIngredientList;
        assertEquals(ingredientList.get(0).getDescription(), addedIngredientList.get(0).getDescription());

        //making some edits
        solo.clearEditText((EditText) solo.getView(R.id.recipeEditName));
        solo.clearEditText((EditText) solo.getView(R.id.recipeEditPrepTime));
        solo.clearEditText((EditText) solo.getView(R.id.recipeEditServings));
        solo.enterText((EditText) solo.getView(R.id.recipeEditName),"TestRecipe2");
        solo.enterText((EditText) solo.getView(R.id.recipeEditPrepTime),"100");
        solo.enterText((EditText) solo.getView(R.id.recipeEditServings),"6");
        solo.clickOnView(solo.getView(R.id.recipeConfirmEdit));

        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);
        solo.waitForText("TestRecipe2",1,500);
        solo.clickOnText("TestRecipe2"); //also serves to check if Recipe was successfully added
        solo.assertCurrentActivity("Wrong Activity", ViewRecipeActivity.class);
        solo.sleep(1500);

        //testing deletion
        solo.clickOnView(solo.getView(R.id.recipeDelete));
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);
        assertFalse(solo.waitForText("TestRecipe2",1,500)); //check that recipe was successfully deleted
        solo.sleep(1500);
    }


    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
