package com.example.menuscript;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.text.InputType;
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

public class IngredientListActivityTest {
    private Solo solo;
    ArrayList<Ingredient> ingredients;
    Ingredient ingredient;

    @Rule
    public ActivityTestRule<IngredientListActivity> rule =
            new ActivityTestRule<IngredientListActivity>(IngredientListActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

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

    @Test
    public void clickTestAddEditDeleteIngredient(){
        solo.assertCurrentActivity("Wrong Activity", IngredientListActivity.class);
        solo.clickOnView(solo.getView(R.id.add_item_button));
        solo.assertCurrentActivity("Wrong Activity", AddIngredientActivity.class);
        solo.sleep(1000);
        //solo.enterText((EditText) solo.getView(R.id.recipeAddTitle),"TestRecipe");
        // solo.clickOnText("TestRecipe");
        solo.enterText((EditText) solo.getView(R.id.itemDescriptionEditText), "Black Garlic");
        solo.enterText((EditText) solo.getView(R.id.countEditText), "4");

        /*
        solo.clickOnView(solo.getView(R.id.bestBeforeEditText));
        solo.sleep(3000);
        solo.clickOnText("OK");
        solo.sleep(2000);
        solo.pressSoftKeyboardSearchButton();
        solo.sleep(2000);
         */ //omitted due to problems with the keyboard

        solo.clickOnView(solo.getView(R.id.unitSpinner));
        solo.sleep(1000);
        solo.clickOnText("grams");
        solo.sleep(1000);

        solo.clickOnView(solo.getView(R.id.locationSpinner));
        solo.sleep(1000);
        solo.clickOnText("fridge");
        solo.sleep(1000);

        solo.clickOnView(solo.getView(R.id.categorySpinner));
        solo.sleep(1000);
        solo.clickOnText("Dairy");
        solo.sleep(3000);

        solo.clickOnText("Submit");
        solo.assertCurrentActivity("Wrong Activity", IngredientListActivity.class);

        solo.waitForText("Black Garlic",1,1000);
        solo.clickOnText("Black Garlic");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientActivity.class);

        assertTrue(solo.waitForText("Black Garlic",1,500));
        assertTrue(solo.waitForText("4",1,500));
        assertTrue(solo.waitForText("grams",1,500));
        assertTrue(solo.waitForText("fridge",1,500));
        assertTrue(solo.waitForText("Dairy",1,500));

        solo.clearEditText((EditText) solo.getView(R.id.itemDescriptionEditText));
        solo.clearEditText((EditText) solo.getView(R.id.countEditText));
        solo.enterText((EditText) solo.getView(R.id.itemDescriptionEditText),"Charred Garlic");
        solo.enterText((EditText) solo.getView(R.id.countEditText),"3");
        solo.sleep(1000);
        solo.clickOnText("fridge");
        solo.sleep(1000);
        solo.clickOnText("pantry");

        solo.clickOnText("Submit");
        solo.assertCurrentActivity("Wrong Activity", IngredientListActivity.class);
        solo.waitForText("Charred Garlic",1,1000);
        solo.clickOnText("Charred Garlic");
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientActivity.class);

        solo.clickOnView(solo.getView(R.id.deleteButton));
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", IngredientListActivity.class);
        assertFalse(solo.waitForText("Charred Garlic",1,500)); //check that recipe was successfully deleted

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
