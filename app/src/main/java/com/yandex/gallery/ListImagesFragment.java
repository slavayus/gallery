package com.yandex.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        updateUI();

        return view;
    }

    private void updateUI() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            data.add(String.valueOf(i));
        }
        ImageAdapter imageAdapter = new ImageAdapter(data);
        mImagesRecyclerView.setAdapter(imageAdapter);
    }

    private class ImagesHolder extends RecyclerView.ViewHolder {
        private final TextView mTextView;
        private String mElement;

        ImagesHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_image, parent, false));

            mTextView = itemView.findViewById(R.id.textView);
        }

        void bind(String element) {
            this.mElement = element;

            mTextView.setText(element);
        }
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImagesHolder> {
        private List<String> mData;

        ImageAdapter(List<String> data) {
            this.mData = data;
        }

        @Override
        public ImagesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ImagesHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ImagesHolder holder, int position) {
            String value = mData.get(position);
            holder.bind(value);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

}
