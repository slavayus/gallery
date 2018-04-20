package com.yandex.gallery;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.yandex.disk.rest.json.Resource;
import com.yandex.gallery.helper.ImageHelper;
import com.yandex.gallery.tasks.BackgroundResponse;
import com.yandex.gallery.tasks.DownloadImagesTask;
import com.yandex.gallery.tasks.LastUploadedTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slavik on 4/16/18.
 */

public class ListImagesFragment extends Fragment {
    private static final String LOG_TAG = "ListImagesFragment";

    private String mToken;
    private RecyclerView mImagesRecyclerView;
    private Point mDisplay;
    private Bitmap mEmptyBitmap;
    private ImageAdapter mImageAdapter;
    private int mCurrentImageIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mToken = getArguments().getString("token");
        this.mDisplay = calculateDisplaySize(this);
        this.mEmptyBitmap = ImageHelper.createEmptyImage(mDisplay, this);
        this.mCurrentImageIndex = 0;
    }

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

//        new FlatResourceListTask(this, mCurrentImageIndex).execute(mToken);
        new LastUploadedTask(this, mCurrentImageIndex).execute(mToken);

        updateUI();

        return view;
    }

    public void onGetLastUploadedImages(BackgroundResponse response) {
        switch (response.getStatus()) {
            case OK: {
                new DownloadImagesTask((Resource) response.getData(), this).execute(mToken);
//                new DownloadPreviewImagesTask((List<Resource>) response.getData(), this).execute();
                break;
            }
            //TODO:dialog
            case ERROR:
        }
    }


    public void onDownloadImages(BackgroundResponse response) {
        switch (response.getStatus()) {
            case OK: {
                Bitmap bitmap = ImageHelper.decodeImages((ByteArrayOutputStream) response.getData(), mDisplay);

                mImageAdapter.updateItem(bitmap);

//                    new FlatResourceListTask(this, ++mCurrentImageIndex).execute(mToken);
                new LastUploadedTask(this, ++mCurrentImageIndex).execute(mToken);

                break;
            }
            //TODO:dialog
            case ERROR:
        }
    }

    private void updateUI() {
        List<Bitmap> bitmaps = new ArrayList<>();
        bitmaps.add(mEmptyBitmap);
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

        void bind(Bitmap leftImage, Bitmap rightImage) {
            mImageViewLeft.setImageBitmap(leftImage);
            mImageViewRight.setImageBitmap(rightImage);
            setLeftImageListener();
            setRightImageListener();
        }

        private void setLeftImageListener() {
            mImageViewLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "Left image clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void setRightImageListener() {
            mImageViewRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "Right image clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }


        void bind(Bitmap leftImage) {
            mImageViewLeft.setImageBitmap(leftImage);
            setLeftImageListener();
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


        void updateItem(Bitmap bitmap) {
            this.mData.add(mCurrentImageIndex, bitmap);
            this.notifyDataSetChanged();
            Log.d(LOG_TAG, "data set notified");
        }
    }

}