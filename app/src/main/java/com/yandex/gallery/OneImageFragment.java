package com.yandex.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yandex.gallery.helper.Images;

/**
 * Created by slavik on 4/16/18.
 */

public class OneImageFragment extends Fragment {
    private static final String LOG_TAG = "OneImageFragment";
    private Bitmap mImage;
    private static final String IMAGE_INDEX = "image_index";
    private boolean mIsHidden = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int index = getArguments().getInt(IMAGE_INDEX);
        byte[] image = Images.getImage(index).toByteArray();
        this.mImage = BitmapFactory.decodeByteArray(image, 0, image.length);
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
                    if (mIsHidden) {
                        actionBar.show();
                    } else {
                        actionBar.hide();
                    }
                    mIsHidden = !mIsHidden;
                }
            }
        });

        ImageView imageView = view.findViewById(R.id.one_image_view);
        imageView.setImageBitmap(mImage);

        return view;
    }

    public static OneImageFragment newInstance(final int index) {
        Bundle args = new Bundle();
        args.putInt(IMAGE_INDEX, index);

        OneImageFragment oneImageFragment = new OneImageFragment();
        oneImageFragment.setArguments(args);
        return oneImageFragment;
    }

}
