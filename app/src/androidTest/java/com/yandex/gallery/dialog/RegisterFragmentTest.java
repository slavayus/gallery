package com.yandex.gallery.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import com.yandex.gallery.LaunchActivity;
import com.yandex.gallery.R;
import com.yandex.gallery.helper.OAuthHelper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Testing RegisterFragment class
 */
public class RegisterFragmentTest {
    private SharedPreferences.Editor preferencesEditor;
    private Intent intent;

    @Rule
    public ActivityTestRule<LaunchActivity> mActivityRule = new ActivityTestRule<>(
            LaunchActivity.class,
            true,
            false);

    @Before
    public void sutUp() {
        intent = new Intent();

        Context appContext = InstrumentationRegistry.getTargetContext();
        preferencesEditor = PreferenceManager.getDefaultSharedPreferences(appContext).edit();
    }


    @Test
    public void createFragment() throws Exception {
        preferencesEditor.clear().apply();

        mActivityRule.launchActivity(intent);

        onView(withText(R.string.register_fragment_title))
                .check(matches(isDisplayed()));
        onView(withText(R.string.register_fragment_text))
                .check(matches(isDisplayed()));
        onView(withText(android.R.string.ok))
                .check(matches(isDisplayed()));
        onView(withText(android.R.string.cancel))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testClickOkButton() throws Exception {
        preferencesEditor.clear().apply();

        mActivityRule.launchActivity(intent);

        Intents.init();

        onView(withText(android.R.string.ok))
                .perform(click());
        intended(allOf(hasAction(Intent.ACTION_VIEW), hasData(OAuthHelper.getUri())));

        Intents.release();
    }
}