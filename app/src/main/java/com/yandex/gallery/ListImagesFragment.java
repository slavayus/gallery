package com.yandex.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.yandex.gallery.tasks.BackgroundResponse;
import com.yandex.gallery.tasks.BackgroundStatus;
import com.yandex.gallery.tasks.DownloadImagesTask;
import com.yandex.gallery.tasks.LastUploadedTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slavik on 4/16/18.
 */

public class ListImagesFragment extends Fragment {

    private String mToken;
    private RecyclerView mImagesRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mToken = getArguments().getString("token");
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
                List<Bitmap> bitmaps = decodeResponse(((List<ByteArrayOutputStream>) response.getData()));
                updateUI(bitmaps);
                break;
            }
            //TODO:dialog
            case ERROR:
        }
    }

    private List<Bitmap> decodeResponse(List<ByteArrayOutputStream> data) {
        List<Bitmap> bitmaps = new ArrayList<>();
        for (ByteArrayOutputStream byteArrayOutputStream : data) {
            bitmaps.add(BitmapFactory.decodeStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())));
        }
        return bitmaps;
    }

    private void updateUI(List<Bitmap> data) {
        ImageAdapter imageAdapter = new ImageAdapter(data);
        mImagesRecyclerView.setAdapter(imageAdapter);
    }

    private class ImagesHolder extends RecyclerView.ViewHolder {
        private final ImageView mImageView;

        ImagesHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_image, parent, false));

            mImageView = itemView.findViewById(R.id.list_item_image);
        }

        void bind(Bitmap image) {
            mImageView.setImageBitmap(image);
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
            Bitmap value = mData.get(position);
            holder.bind(value);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

}
