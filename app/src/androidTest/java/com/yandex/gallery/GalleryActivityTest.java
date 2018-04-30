package com.yandex.gallery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;

import com.yandex.gallery.helper.OAuthHelper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Testing GalleryActivity class
 */
public class GalleryActivityTest {
    private Intent intent;
    private SharedPreferences preferences;

    @Rule
    public ActivityTestRule<GalleryActivity> mActivityRule = new ActivityTestRule<>(
            GalleryActivity.class,
            false,
            false);

    @Before
    public void sutUp() {
        intent = new Intent();

        Context appContext = InstrumentationRegistry.getTargetContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    // TODO: 5/1/18 check the opening of a new activity
    @Test(expected = NoMatchingViewException.class)
    public void createFragment() throws Exception {
        intent.setData(Uri.parse("access_token=aaaaaaa&token_type=bearer&expires_in=23132312"));
        mActivityRule.launchActivity(intent);
        onView(withText(R.string.register_fragment_title));

        String token = getTokenFromProperties();
        mActivityRule.finishActivity();

        intent.setData(Uri.parse("access_tokenasd=" + token + "&token_type=bearer&expires_in=31534144"));
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.images_recycler_view))
                .check(matches(isDisplayed()));
        mActivityRule.finishActivity();

        intent.setData(Uri.parse("access_token=asdf" + token + "&token_type=bearer&expires_in=31534144"));
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.images_recycler_view))
                .check(matches(isDisplayed()));
        mActivityRule.finishActivity();
    }

    private String getTokenFromProperties() throws IOException {
        Properties properties = new Properties();
        AssetManager assetManager = mActivityRule.getActivity().getAssets();
        InputStream inputStream = assetManager.open("token.properties");
        properties.load(inputStream);
        return properties.getProperty("token");
    }

    @Test
    public void saveToken() throws Exception {
        GalleryActivity galleryActivity = mActivityRule.launchActivity(intent);

        preferences.edit().clear().apply();
        galleryActivity.saveToken("YEE");
        String token = preferences.getString(OAuthHelper.RESPONSE_TYPE, null);
        assertNotNull(token);
        assertEquals("YEE", token);

        preferences.edit().clear().apply();
        token = preferences.getString(OAuthHelper.RESPONSE_TYPE, null);
        assertNull(token);

        preferences.edit().clear().apply();
        galleryActivity.saveToken("");
        token = preferences.getString(OAuthHelper.RESPONSE_TYPE, null);
        assertNotEquals("\n", token);

        preferences.edit().clear().apply();
        mActivityRule.finishActivity();
    }


}