package com.yandex.gallery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.yandex.gallery.helper.OAuthHelper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Testing LaunchActivity class
 */
@RunWith(AndroidJUnit4.class)
public class LaunchActivityTest {
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

        ExpectedException exception = ExpectedException.none();

        preferencesEditor.putString(OAuthHelper.RESPONSE_TYPE, "aaaaaaaaaaaaaaBaaaaa").apply();
        mActivityRule.launchActivity(intent);
        exception.expect(NoMatchingViewException.class);
        onView(withId(R.id.images_recycler_view));
    }


    @Test
    public void getToken() throws Exception {
        LaunchActivity launchActivity = mActivityRule.launchActivity(intent);

        preferencesEditor.clear().apply();
        String token = launchActivity.getToken();
        assertNull(token);

        preferencesEditor.putString("YEE", "YEE").apply();
        token = launchActivity.getToken();
        assertNull(token);

        preferencesEditor.putString(OAuthHelper.RESPONSE_TYPE, "token").apply();
        token = launchActivity.getToken();
        assertEquals("token", token);

        preferencesEditor.putString(OAuthHelper.RESPONSE_TYPE, "").apply();
        token = launchActivity.getToken();
        assertEquals("", token);

        preferencesEditor.putString(OAuthHelper.RESPONSE_TYPE, null).apply();
        token = launchActivity.getToken();
        assertNull(token);

        preferencesEditor.clear().apply();
    }

}