package com.yandex.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.yandex.gallery.helper.Images;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by slavik on 4/21/18.
 */

public class OneImagePagerActivity extends AppCompatActivity {
    private static final String EXTRA_IMAGE = "com.yandex.gallery.extra_image";

    private ViewPager mViewPager;
    private List<ByteArrayOutputStream> mImages;
    private int mIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_image_pager);

        mIndex = getIntent().getIntExtra(EXTRA_IMAGE, 0);
        mViewPager = findViewById(R.id.one_image_view_pager);
        mImages = Images.getAll();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return OneImageFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return mImages.size();
            }
        });

        mViewPager.setCurrentItem(mIndex);
    }

    public static Intent newIntent(Context context, int position) {
        Intent intent = new Intent(context, OneImagePagerActivity.class);
        intent.putExtra(EXTRA_IMAGE, position);
        return intent;
    }
}
