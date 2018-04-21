package com.yandex.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yandex.gallery.dialog.RegisterFragment;

/**
 * Created by slavik on 4/15/18.
 */

public class GalleryFragment extends Fragment {
    private static final String LOG_TAG = "GalleryFragment";
    private static final String REGISTER_DIALOG = "RegisterFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragment_gallery = inflater.inflate(R.layout.fragment_gallery, container, false);

        FragmentManager fragmentManager = getFragmentManager();
        RegisterFragment registerFragment = new RegisterFragment();
        registerFragment.show(fragmentManager, REGISTER_DIALOG);

        return fragment_gallery;
    }
}
