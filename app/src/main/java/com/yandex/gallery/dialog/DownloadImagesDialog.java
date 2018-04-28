package com.yandex.gallery.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import com.yandex.gallery.R;

/**
 * Dialog for errors which throws while work with yandex server.
 */

public class DownloadImagesDialog extends DialogFragment {
    private static final String LOG_TAG = "DownloadImagesDialog";
    private static final String MESSAGE = "MESSAGE";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = getArguments().getString(MESSAGE);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.yandex_server_error_title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finishAffinity();
                    }
                })
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            getActivity().finishAffinity();
                        }
                        return false;
                    }
                })
                .create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        return alertDialog;
    }


    /**
     * Creates a new DownloadImagesDialog with the given body for displaying.
     *
     * @param body the string message which will be displayed in dialog
     * @return a new instance of DownloadImagesDialog
     */
    public static DownloadImagesDialog newInstance(String body) {
        DownloadImagesDialog downloadImagesDialog = new DownloadImagesDialog();
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE, body);
        downloadImagesDialog.setArguments(bundle);
        return downloadImagesDialog;
    }
}
