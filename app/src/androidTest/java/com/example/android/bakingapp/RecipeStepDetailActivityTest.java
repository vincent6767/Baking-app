package com.example.android.bakingapp;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.entities.Ingredient;
import com.example.android.bakingapp.entities.Recipe;
import com.example.android.bakingapp.entities.RecipeStep;
import com.example.android.bakingapp.ui.RecipeStepDetailActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class RecipeStepDetailActivityTest {
    @Rule
    public ActivityTestRule<RecipeStepDetailActivity> mActivityTestRule = new ActivityTestRule<>(RecipeStepDetailActivity.class, true, false);
    private RecipeStep mFirstStep;
    private RecipeStep mSecondStep;
    private RecipeStep mThirdStep;

    @Before
    public void setupRecipeStepDetalActivity() {
        List<Ingredient> ingredientList = new ArrayList<>();
        Ingredient ingredient = new Ingredient(2.0f, "CUP", "Graham Cracker crumbs");
        ingredientList.add(ingredient);
        List<RecipeStep> recipeSteps = new ArrayList<>();
        mFirstStep = new RecipeStep(0, "Recipe Introduction",
                "Recipe Introduction",
                "\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4\"",
                ""
        );
        mSecondStep = new RecipeStep(1, "Starting prep",
                "1. Preheat the oven to 350°F. Butter a 9\\\" deep dish pie pan.",
                "",
                ""
        );
        mThirdStep = new RecipeStep(2, "Melt butter and bittersweet chocolate",
                "2. Melt the butter and bittersweet chocolate together in a microwave or a double boiler. If microwaving, heat for 30 seconds at a time, removing bowl and stirring ingredients in between.",
                "\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdc43_1-melt-choclate-chips-and-butter-brownies/1-melt-choclate-chips-and-butter-brownies.mp4\"",
                ""
        );

        recipeSteps.add(mFirstStep);
        recipeSteps.add(mSecondStep);
        recipeSteps.add(mThirdStep);

        Recipe recipe = new Recipe(1, "Nutella Pie", ingredientList, recipeSteps, 8, "");

        Intent intent = new Intent();
        intent.putExtra(RecipeStepDetailActivity.RECIPE_STEP_KEY, mSecondStep);
        intent.putExtra(RecipeStepDetailActivity.RECIPE_KEY, recipe);
        mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void clickNextButton_NavigateToTheNextStep() {
        onView(withId(R.id.btn_next_step)).perform(click());

        onView(withId(R.id.btn_previous_step)).check(matches(isEnabled()));
        onView(withId(R.id.btn_next_step)).check(matches(not(isEnabled())));
        onView(withId(R.id.tv_recipe_step_description)).check(matches(withText(mThirdStep.getDescription())));
    }

    @Test
    public void clickPreviousButton_NavigateToThePreviousStep() {
        onView(withId(R.id.btn_previous_step)).perform(click());

        onView(withId(R.id.btn_previous_step)).check(matches(not(isEnabled())));
        onView(withId(R.id.btn_next_step)).check(matches(isEnabled()));

        onView(withId(R.id.tv_recipe_step_description)).check(matches(withText(mFirstStep.getDescription())));
    }

    @Test
    public void clickDisabledNextButton_NothingHappen() {
        onView(withId(R.id.btn_next_step)).perform(click());

        onView(withId(R.id.btn_next_step)).perform(click());
        onView(withId(R.id.tv_recipe_step_description)).check(matches(withText(mThirdStep.getDescription())));
    }

    @Test
    public void clickDisabledPreviousButton_NothingHappen() {
        onView(withId(R.id.btn_previous_step)).perform(click());

        onView(withId(R.id.btn_previous_step)).perform(click());
        onView(withId(R.id.tv_recipe_step_description)).check(matches(withText(mFirstStep.getDescription())));
    }

    @Test
    public void navigateToStepThatHasNoVideo_SeeErrorMessage() {
        // Already on a step that has no Video URL.
        onView(withId(R.id.tv_video_error_message)).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToStepThatHasVideo_SeeNoErrorMessage() {
        onView(withId(R.id.btn_previous_step)).perform(click());

        onView(withId(R.id.tv_video_error_message)).check(matches(not(isDisplayed())));
    }
}
