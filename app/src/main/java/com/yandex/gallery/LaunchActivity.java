package com.yandex.gallery;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;

import static com.yandex.gallery.GalleryActivity.SAVED_TOKEN;

/**
 * App start activity
 */

public class LaunchActivity extends SingleFragmentActivity {
    private static final String LOG_TAG = "LaunchActivity";

    @Override
    protected Fragment createFragment() {
        String mToken = getToken();

        if (mToken != null) {
            return ListImagesFragment.newInstance(mToken);
        }

        return new GalleryFragment();
    }


    /**
     * Load saved 0Auth token
     *
     * @return 0Auth token
     */
    public String getToken() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = preferences.getString(SAVED_TOKEN, null);
        Log.d(LOG_TAG, " token loaded");
        return token;
    }
}
