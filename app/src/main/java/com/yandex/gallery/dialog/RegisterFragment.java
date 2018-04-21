package com.yandex.gallery.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.yandex.gallery.R;
import com.yandex.gallery.helper.OAuthHelper;

/**
 * Created by slavik on 4/21/18.
 */

public class RegisterFragment extends DialogFragment {
    private static final String LOG_TAG = "RegisterFragment";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.register_fragment_title)
                .setMessage(R.string.register_fragment_text)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(LOG_TAG, " on click");
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(OAuthHelper.getUri())));
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }
}
