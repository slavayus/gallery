package com.yandex.gallery;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yandex.gallery.helper.ImageHelper;
import com.yandex.gallery.helper.Images;

/**
 * Fragment for displaying image on full display
 */

public class OneImageFragment extends Fragment {
    private static final String LOG_TAG = "OneImageFragment";
    private Bitmap mImage;
    private static final String IMAGE_INDEX = "image_index";
    private boolean mIsHiddenActionBar = false;
    private ImageView mImageView;
    private int currentDegrees = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int index = getArguments().getInt(IMAGE_INDEX);
        this.mImage = ImageHelper.decodeImage(Images.instance().getImage(index));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_image, container, false);

        setUpRotateButtonListeners(view);

        //TODO: animate rotate buttons
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

    /**
     * Activity lifecycle method.
     * Setup the image into view
     *
     * @param savedInstanceState saved activity state
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImageView.setImageBitmap(mImage);
    }


    /**
     * Activity lifecycle method
     * Clear unnecessary image
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mImage.recycle();
        mImage = null;
        System.gc();
        Log.d(LOG_TAG, "destroyed");
    }

    /**
     * Creates a new OneImageFragment with the given image index for displaying.
     *
     * @param index the image index in store {@link Images}
     * @return a new instance of OneImageFragment
     */
    public static OneImageFragment newInstance(final int index) {
        Bundle args = new Bundle();
        args.putInt(IMAGE_INDEX, index);

        OneImageFragment oneImageFragment = new OneImageFragment();
        oneImageFragment.setArguments(args);
        return oneImageFragment;
    }


    /**
     * Setup rotate button listeners
     *
     * @param rootView view where are located rotation buttons
     */
    public void setUpRotateButtonListeners(View rootView) {
        ((BottomNavigationView) rootView.findViewById(R.id.bottom_rotate_buttons))
                .setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.rotate_left_button:
                                mImageView.setImageBitmap(ImageHelper.rotateImage(mImage, currentDegrees -= 90));
                                break;
                            case R.id.rotate_right_button:
                                mImageView.setImageBitmap(ImageHelper.rotateImage(mImage, currentDegrees += 90));
                                break;
                        }
                        return false;
                    }
                });
    }
}
