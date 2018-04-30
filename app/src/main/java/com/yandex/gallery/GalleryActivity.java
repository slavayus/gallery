package com.yandex.gallery;

import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.yandex.gallery.helper.OAuthHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This activity start when user apply agreement of yandex.disk
 */

public class GalleryActivity extends SingleFragmentActivity {
    private static final String LOG_TAG = "GalleryActivity";

    /**
     * Create fragment for displaying images if user applied agreements of yandex.disk
     * otherwise start again registration dialog
     *
     * @return new fragment
     * @see ListImagesFragment
     * @see GalleryFragment
     */
    @Override
    protected Fragment createFragment() {
        Uri data = getIntent().getData();
        if (data != null) {
            Log.d(LOG_TAG, data.toString());
            Pattern pattern = Pattern.compile("access_token=(.*?)(&|$)");
            Matcher matcher = pattern.matcher(data.toString());
            if (matcher.find()) {

                String mToken = matcher.group(1);

                saveToken(mToken);

                return ListImagesFragment.newInstance(mToken);
            }
        } else {
            Log.d(LOG_TAG, "data is null");
        }

        return new GalleryFragment();
    }

    /**
     * Save received token
     *
     * @param token yandex 0Auth token which will be saved
     */
    protected void saveToken(String token) {
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .edit()
                .putString(OAuthHelper.RESPONSE_TYPE, token)
                .apply();
        Log.d(LOG_TAG, " token saved");
    }
}