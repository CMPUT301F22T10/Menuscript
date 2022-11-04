package com.example.menuscript;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test class for RecipeListActivity. All the UI tests are written here. Robotium test framework is
 used
 * @author Wanlin
 */
public class RecipeListActivityTest {
    private Solo solo;
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
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
