package com.yandex.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


public class GalleryActivity extends AppCompatActivity {
    private static final String LOG_TAG = "GalleryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.gallery_container);
        if (fragment == null) {
            fragment = new GalleryFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.gallery_container, fragment)
                    .commit();
        }
    }

}
