package com.yandex.gallery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yandex.disk.rest.json.Resource;
import com.yandex.gallery.dialog.DownloadImagesDialog;
import com.yandex.gallery.helper.ImageHelper;
import com.yandex.gallery.helper.Images;
import com.yandex.gallery.listeners.DownloadImageListener;
import com.yandex.gallery.listeners.LastUploadedImagesListener;
import com.yandex.gallery.tasks.BackgroundResponse;
import com.yandex.gallery.tasks.DownloadImagesTask;
import com.yandex.gallery.tasks.LastUploadedTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for displaying all the images
 */

public class ListImagesFragment extends Fragment implements DownloadImageListener, LastUploadedImagesListener {
    private static final String LOG_TAG = "ListImagesFragment";
    private static final String TOKEN = "TOKEN";
    private static final String YANDEX_SERVER_ERROR_DIALOG = "YANDEX_SERVER_ERROR_DIALOG";

    private String mToken;
    private RecyclerView mImagesRecyclerView;
    private Point mDisplay;
    private Bitmap mEmptyBitmap;
    private ImageAdapter mImageAdapter;
    private int mCurrentImageIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mToken = getArguments().getString(TOKEN);
        this.mDisplay = calculateDisplaySize(this);
        this.mEmptyBitmap = ImageHelper.createEmptyImage(mDisplay, getResources().getColor(R.color.emptyImageColor));
        this.mCurrentImageIndex = 0;
    }


    /**
     * Calculate the display size of the device
     *
     * @param fragment current fragment
     * @return {@link Point} with the display size of the user device
     */
    public Point calculateDisplaySize(Fragment fragment) {
        Display display = fragment.getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_images, container, false);
        mImagesRecyclerView = view.findViewById(R.id.images_recycler_view);
        mImagesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new LastUploadedTask(this, mCurrentImageIndex++).execute(mToken);

        updateUI();

        return view;
    }


    /**
     * Handler response from AsyncTask
     *
     * @param response from {@link LastUploadedTask}
     */
    @Override
    public void onGetLastUploadedImages(BackgroundResponse response) {
        switch (response.getStatus()) {
            case OK: {
                new DownloadImagesTask((Resource) response.getData(), this).execute(mToken);
                break;
            }
            case ERROR: {
                DownloadImagesDialog downloadImagesDialog = DownloadImagesDialog.
                        newInstance(response.getMessage());
                if (downloadImagesDialog != null && getFragmentManager() != null) {
                    downloadImagesDialog.show(getFragmentManager(), YANDEX_SERVER_ERROR_DIALOG);
                }
                clearPreferences();
            }
        }
    }

    /**
     * Clear all saved preferences
     */
    private void clearPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferences.edit().clear().apply();
    }

    /**
     * Handler response from AsyncTask
     *
     * @param response from {@link DownloadImagesTask}
     */
    @Override
    public void onDownloadImages(BackgroundResponse response) {
        switch (response.getStatus()) {
            case OK: {
                ByteArrayOutputStream responseData = (ByteArrayOutputStream) response.getData();

                Images.instance().addImage(responseData);

                Bitmap bitmap = ImageHelper.decodeImageRegion(responseData, mDisplay, this.getActivity());

                mImageAdapter.addImage(bitmap);
                mImageAdapter.updateItem();

                updateSubtitle();

                OneImagePagerActivity.notifyAdapter();

                if (mCurrentImageIndex < 36) {
                    //                    new FlatResourceListTask(this, ++mCurrentImageIndex).execute(mToken);
                    new LastUploadedTask(this, mCurrentImageIndex++).execute(mToken);
                }

                break;
            }
            case ERROR: {
                DownloadImagesDialog.
                        newInstance(response.getMessage()).
                        show(getFragmentManager(), YANDEX_SERVER_ERROR_DIALOG);
            }
        }
    }

    /**
     * Update activity subtitle in action bar
     */
    private void updateSubtitle() {
        if (getActivity() != null) {
            String subtitle = getString(R.string.subtitle_format, mCurrentImageIndex);
            ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setSubtitle(subtitle);
            }
        }
    }

    /**
     * Setup starting images
     */
    private void updateUI() {
        List<Bitmap> bitmaps = new ArrayList<>();
        bitmaps.add(mEmptyBitmap);
        bitmaps.add(null);

        mImageAdapter = new ImageAdapter(bitmaps);
        mImagesRecyclerView.setAdapter(mImageAdapter);
    }

    private class ImagesHolder extends RecyclerView.ViewHolder {
        private final ImageView mImageViewLeft;
        private final ImageView mImageViewRight;

        ImagesHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_image, parent, false));

            mImageViewLeft = itemView.findViewById(R.id.list_item_image_left);
            mImageViewRight = itemView.findViewById(R.id.list_item_image_right);
        }

        void bind(Bitmap leftImage, Bitmap rightImage, final int position) {
            mImageViewLeft.setImageBitmap(leftImage);
            mImageViewRight.setImageBitmap(rightImage);
            setImageListeners(position);
        }

        void doOnClick(int position) {
            Intent intent = OneImagePagerActivity.newIntent(getActivity(), position);
            startActivity(intent);
        }

        private void setImageListeners(final int position) {
            mImageViewLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doOnClick(position);
                }
            });

            mImageViewRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doOnClick(position + 1);
                }
            });
        }
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImagesHolder> {
        private List<Bitmap> mData;

        ImageAdapter(List<Bitmap> data) {
            this.mData = data;
        }

        @Override
        public ImagesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ImagesHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ImagesHolder holder, int position) {
            holder.bind(mData.get(2 * position), mData.get(2 * position + 1), 2 * position);
        }

        @Override
        public int getItemCount() {
            return mData.size() / 2;
        }

        void addImage(Bitmap bitmap) {
            this.mData.add(mCurrentImageIndex - 1, bitmap);
        }

        void updateItem() {
            this.notifyItemChanged(mCurrentImageIndex / 2);
            this.notifyItemChanged(((mCurrentImageIndex - 1) / 2));
            Log.d(LOG_TAG, "data set notified");
        }

    }

    /**
     * Creates a new ListImagesFragment with the given 0Auth token.
     *
     * @param token 0Auth token
     * @return a new instance of OneImageFragment
     * @see com.yandex.gallery.helper.OAuthHelper#RESPONSE_TYPE
     */
    public static ListImagesFragment newInstance(String token) {
        Bundle args = new Bundle();
        args.putString(TOKEN, token);

        ListImagesFragment listImagesFragment = new ListImagesFragment();
        listImagesFragment.setArguments(args);
        return listImagesFragment;
    }
}