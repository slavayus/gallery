package com.yandex.gallery;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by slavik on 4/16/18.
 */

public class OneImageActivity extends SingleFragmentActivity {
    public static final String EXTRA_IMAGE = "com.yandex.gallery.extra_image";

    @Override
    protected Fragment createFragment() {
        return new OneImageFragment();
    }

    public static Intent newIntent(Context context, int position) {
        Intent intent = new Intent(context, OneImageActivity.class);
        intent.putExtra(EXTRA_IMAGE, position);
        return intent;
    }
}
