package com.yandex.gallery;

import android.support.v4.app.Fragment;

/**
 * Created by slavik on 4/16/18.
 */

public class OneImageActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new OneImageFragment();
    }
}
