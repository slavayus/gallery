package com.yandex.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yandex.disk.rest.json.Resource;
import com.yandex.gallery.helper.ImageHelper;
import com.yandex.gallery.tasks.BackgroundResponse;
import com.yandex.gallery.tasks.DownloadImagesTask;
import com.yandex.gallery.tasks.LastUploadedTask;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by slavik on 4/16/18.
 */

public class ListImagesFragment extends Fragment {
    private static final String LOG_TAG = "ListImagesFragment";

    private String mToken;
    private RecyclerView mImagesRecyclerView;
    private Point mDisplay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mToken = getArguments().getString("token");
        this.mDisplay = ImageHelper.calculateDisplaySize(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_images, container, false);
        mImagesRecyclerView = view.findViewById(R.id.images_recycler_view);
        mImagesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new LastUploadedTask(this).execute(mToken);

        return view;
    }

    public void onGetLastUploadedImages(BackgroundResponse response) {
        switch (response.getStatus()) {
            case OK: {
                new DownloadImagesTask((List<Resource>) response.getData(), this).execute(mToken);
                break;
            }
            //TODO:dialog
            case ERROR:
        }
    }


    public void onDownloadImages(BackgroundResponse response) {
        switch (response.getStatus()) {
            case OK: {
                List<Bitmap> bitmaps = ImageHelper.decodeImages((List<ByteArrayOutputStream>) response.getData(), mDisplay);
                updateUI(bitmaps);
                break;
            }
            //TODO:dialog
            case ERROR:
        }
    }

    private void updateUI(List<Bitmap> data) {
        ImageAdapter imageAdapter = new ImageAdapter(data);
        mImagesRecyclerView.setAdapter(imageAdapter);
    }

    private class ImagesHolder extends RecyclerView.ViewHolder {
        private final ImageView mImageViewLeft;
        private final ImageView mImageViewRight;

        ImagesHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_image, parent, false));

            mImageViewLeft = itemView.findViewById(R.id.list_item_image_left);
            mImageViewRight = itemView.findViewById(R.id.list_item_image_right);
        }

        void bind(Bitmap leftImage, Bitmap rightImage) {
            mImageViewLeft.setImageBitmap(leftImage);
            mImageViewRight.setImageBitmap(rightImage);
        }

        void bind(Bitmap leftImage) {
            mImageViewLeft.setImageBitmap(leftImage);
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
            if ((2 * position + 1) < mData.size()) {
                holder.bind(mData.get(2 * position), mData.get(2 * position + 1));
            } else {
                holder.bind(mData.get(2 * position));
            }
        }

        @Override
        public int getItemCount() {
            return (mData.size() + 1) / 2;
        }
    }

}