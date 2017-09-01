package com.example.android.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.ui.RecipesListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipesListActivityTest {
    @Rule
    public ActivityTestRule<RecipesListActivity> mActivityTestRule = new ActivityTestRule<RecipesListActivity>(RecipesListActivity.class);

    @Test
    public void clickSettings_OpensRecipeWidgetSettingsActivity() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withId(R.id.action_recipe_widget_settings)).perform(click());

        onView(withId(R.id.rg_recipes_list_for_setting)).check(matches(isDisplayed()));
    }
}
