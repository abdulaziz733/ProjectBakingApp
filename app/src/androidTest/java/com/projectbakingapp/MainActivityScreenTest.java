package com.projectbakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.projectbakingapp.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * Created by abdul on 9/7/2017.
 */

//@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void clickGridViewItem_OpensOrderActivity() {

        onView(ViewMatchers.withId(R.id.list_recipe)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(ViewMatchers.withId(R.id.list_recipe)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

    }

}
