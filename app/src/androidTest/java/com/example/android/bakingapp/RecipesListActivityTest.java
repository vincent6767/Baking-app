package com.example.android.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.Suppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.android.bakingapp.idlingresources.RecipesExecutor;
import com.example.android.bakingapp.ui.RecipesListActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class RecipesListActivityTest {
    @Rule
    public ActivityTestRule<RecipesListActivity> mActivityTestRule = new ActivityTestRule<>(RecipesListActivity.class);
    private RecipesExecutor mRecipesExecutor;

    @Suppress
    public void clickSettings_OpensRecipeWidgetSettingsActivity() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withId(R.id.action_recipe_widget_settings)).perform(click());

        onView(withId(R.id.rg_recipes_list_for_setting)).check(matches(isDisplayed()));
    }

    @Before
    public void registerExecutor() {
        IdlingPolicies.setMasterPolicyTimeout(60000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(60000 * 2, TimeUnit.MILLISECONDS);

        mRecipesExecutor = mActivityTestRule.getActivity().getRecipesExecutor();
        mRecipesExecutor.setIdleState(true);
        mActivityTestRule.getActivity().setRecipesExecutor(mRecipesExecutor);

        Espresso.registerIdlingResources(mRecipesExecutor);
    }

    @Test
    public void clickRecipe_ItemsRecyclerViewItemCountShouldBeGreaterThanZero() {
        onView(withId(R.id.rv_recipes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.rv_items_list)).check(new RecyclerViewItemCountAssertion(greaterThan(0)));
    }

    @After
    public void unregisterIdlingResource() {
        if (mRecipesExecutor != null) {
            Espresso.unregisterIdlingResources(mRecipesExecutor);
        }
    }

    class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final Matcher<Integer> mMatcher;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.mMatcher = is(expectedCount);
        }

        RecyclerViewItemCountAssertion(Matcher<Integer> matcher) {
            this.mMatcher = matcher;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }
            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assertThat(adapter.getItemCount(), mMatcher);
        }
    }
}
