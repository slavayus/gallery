package com.yandex.gallery;

import android.support.v4.app.Fragment;


public class GalleryActivity extends SingleFragmentActivity {
    private static final String LOG_TAG = "GalleryActivity";

    @Override
    protected Fragment createFragment() {
        return new GalleryFragment();
    }
}
