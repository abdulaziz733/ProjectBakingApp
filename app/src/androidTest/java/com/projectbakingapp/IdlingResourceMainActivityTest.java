package com.projectbakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.projectbakingapp.activity.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by abdul on 9/7/2017.
 */
@RunWith(AndroidJUnit4.class)
public class IdlingResourceMainActivityTest {

    private static final String YELLOW_CAKE = "Yellow Cake";

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource() {
        idlingResource = mainActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }

    @Test
    public void checkContentRecipe() {
        onView(ViewMatchers.withId(R.id.list_recipe)).perform(RecyclerViewActions.scrollToPosition(2));
        onView(withText(YELLOW_CAKE)).check(matches(isDisplayed()));
    }

//    @Test
//    public void checkPlayerView_Step() {
//        onView(ViewMatchers.withId(R.id.list_recipe)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
////        onView(ViewMatchers.withId(R.id.rv_list_step)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
//        onView(withId(R.id.rv_list_step)).check(matches(isDisplayed()));
//    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }


}
