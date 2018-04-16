package com.yandex.gallery;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListImagesActivity extends SingleFragmentActivity {
    private final String LOG_TAG = getClass().getName();

    @Override
    protected Fragment createFragment() {

        Uri data = getIntent().getData();
        if (data != null) {
            Log.d(LOG_TAG, data.toString());
            Pattern pattern = Pattern.compile("access_token=(.*?)(&|$)");
            Matcher matcher = pattern.matcher(data.toString());
            if (matcher.find()) {

                String token = matcher.group(1);

                Bundle bundle = new Bundle();
                bundle.putString("token", token);

                ListImagesFragment listImagesFragment = new ListImagesFragment();
                listImagesFragment.setArguments(bundle);
                return listImagesFragment;
            }
        } else {
            Log.d(LOG_TAG, "data is null");
        }
        return new ErrorOauthFragment();
    }
}
