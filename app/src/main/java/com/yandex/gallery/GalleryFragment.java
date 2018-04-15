package com.yandex.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yandex.gallery.helper.OAuthHelper;

/**
 * Created by slavik on 4/15/18.
 */

public class GalleryFragment extends Fragment {
    private static final String LOG_TAG = "GalleryFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragment_gallery = inflater.inflate(R.layout.fragment_gallery, container, false);

        Button button = fragment_gallery.findViewById(R.id.browser);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, " on click");
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(OAuthHelper.getUri())));
            }
        });

        return fragment_gallery;
    }
}
