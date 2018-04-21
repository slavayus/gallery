package com.yandex.gallery.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.yandex.gallery.R;

/**
 * Created by slavik on 4/21/18.
 */

public class RegisterFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setView(R.layout.dialog_register)
                .setTitle(R.string.register_fragment_title)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }
}
