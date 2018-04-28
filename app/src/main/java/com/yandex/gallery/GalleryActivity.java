package com.yandex.gallery;

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GalleryActivity extends SingleFragmentActivity {
    private static final String LOG_TAG = "GalleryActivity";
    public static final String SAVED_TOKEN = "SAVED_TOKEN";
    private String mToken;

    @Override
    protected Fragment createFragment() {
        Uri data = getIntent().getData();
        if (data != null) {
            Log.d(LOG_TAG, data.toString());
            Pattern pattern = Pattern.compile("access_token=(.*?)(&|$)");
            Matcher matcher = pattern.matcher(data.toString());
            if (matcher.find()) {

                mToken = matcher.group(1);

                saveToken();

                return ListImagesFragment.newInstance(mToken);
            }
        } else {
            Log.d(LOG_TAG, "data is null");
        }

        return new GalleryFragment();
    }

    private void saveToken() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(SAVED_TOKEN, mToken);
        edit.apply();
        Log.d(LOG_TAG, " token saved");
    }
}