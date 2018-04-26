package com.yandex.gallery;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yandex.gallery.helper.ImageHelper;
import com.yandex.gallery.helper.Images;

/**
 * Created by slavik on 4/16/18.
 */

public class OneImageFragment extends Fragment {
    private static final String LOG_TAG = "OneImageFragment";
    private Bitmap mImage;
    private static final String IMAGE_INDEX = "image_index";
    private boolean mIsHiddenActionBar = false;
    private ImageView mImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int index = getArguments().getInt(IMAGE_INDEX);
        this.mImage = ImageHelper.decodeImage(Images.getImage(index));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_image, container, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                if (actionBar != null) {
                    if (mIsHiddenActionBar) {
                        actionBar.show();
                    } else {
                        actionBar.hide();
                    }
                    mIsHiddenActionBar = !mIsHiddenActionBar;
                }
            }
        });

        mImageView = view.findViewById(R.id.one_image_view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImageView.setImageBitmap(mImage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mImage.recycle();
        mImage = null;
        System.gc();
        Log.d(LOG_TAG, "destroyed");
    }

    public static OneImageFragment newInstance(final int index) {
        Bundle args = new Bundle();
        args.putInt(IMAGE_INDEX, index);

        OneImageFragment oneImageFragment = new OneImageFragment();
        oneImageFragment.setArguments(args);
        return oneImageFragment;
    }
}
