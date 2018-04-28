package com.yandex.gallery.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.ResourcesArgs;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.exceptions.ServerIOException;
import com.yandex.disk.rest.json.Resource;
import com.yandex.disk.rest.json.ResourceList;
import com.yandex.gallery.R;
import com.yandex.gallery.listeners.LastUploadedImagesListener;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

/**
 * Download image from yandex disk by URI
 *
 * @see BackgroundResponse
 */

public class LastUploadedTask extends AsyncTask<String, Void, BackgroundResponse> {
    private final Fragment fragment;
    private final int mCurrentImageIndex;

    /**
     * Creates a new AsyncTask to get resource of image
     *
     * @param currentImageIndex image index which need to download
     * @param fragment          instance of Fragment
     * @see Resource
     */
    public LastUploadedTask(Fragment fragment, int currentImageIndex) {
        this.fragment = fragment;
        this.mCurrentImageIndex = currentImageIndex;
    }

    /**
     * Get image resource
     *
     * @param data 0Auth token for authorization
     * @return task status
     */
    @Override
    protected BackgroundResponse doInBackground(String... data) {
        try {
            Credentials credentials = new Credentials("", data[0]);

            RestClient restClient = new RestClient(credentials);
            ResourceList resourceList = restClient.getLastUploadedResources(new ResourcesArgs.Builder().setMediaType("image").setLimit(mCurrentImageIndex + 1).build());

            return new BackgroundResponse<Resource>(BackgroundStatus.OK).addData(resourceList.getItems().get(mCurrentImageIndex));
        } catch (IOException e) {
            e.printStackTrace();
            return new BackgroundResponse(BackgroundStatus.ERROR)
                    .addMessage(fragment.getString(R.string.network_error_text));

        } catch (ServerIOException e) {
            e.printStackTrace();
            clearPreferences();
            return new BackgroundResponse(BackgroundStatus.ERROR)
                    .addMessage(fragment.getString(R.string.yandex_server_error_text));
        }
    }

    private void clearPreferences() {
        SharedPreferences preferences = fragment.getActivity().getPreferences(MODE_PRIVATE);
        preferences.edit().clear().apply();
    }

    @Override
    protected void onPostExecute(BackgroundResponse response) {
        ((LastUploadedImagesListener) fragment).onGetLastUploadedImages(response);
    }
}
