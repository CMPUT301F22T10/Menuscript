package com.example.menuscript;

import android.app.Activity;
import android.view.View;
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
 * Test class for AddIngredientMealPlan activity. All the UI tests are written here. Robotium test framework is
 used
 * @author Wanlin
 */

public class AddIngredientMealPlanActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<AddIngredientMealPlanActivity> rule =
            new ActivityTestRule<>(AddIngredientMealPlanActivity.class, true, true);

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

    /**
     *
     */
    @Test
    public void checkSpinnerSelect(){
        solo.assertCurrentActivity("Wrong Activity", AddIngredientMealPlanActivity.class);
        View view1 = solo.getView(Spinner.class,0);
        solo.clickOnView(view1);
        solo.scrollToTop();
        solo.pressSpinnerItem(0,0);
    }

    /**
     * Check whether activity correctly switched to add new ingredient
     */

    @Test
    public void checkClickAddNewIngredient(){
        solo.assertCurrentActivity("Wrong Activity", AddIngredientMealPlanActivity.class);
        solo.clickOnButton("Add New Ingredient");
        solo.assertCurrentActivity("Wrong Activity", AddIngredientActivity.class);
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

