package com.yandex.gallery.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.ResourcesArgs;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.exceptions.ServerIOException;
import com.yandex.disk.rest.json.Resource;
import com.yandex.disk.rest.json.ResourceList;
import com.yandex.gallery.ListImagesFragment;
import com.yandex.gallery.R;
import com.yandex.gallery.dialog.YandexServerErrorDialog;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by slavik on 4/16/18.
 */

public class LastUploadedTask extends AsyncTask<String, Void, BackgroundResponse> {
    private final ListImagesFragment listImagesFragment;
    private final int mCurrentImageIndex;
    private static final String YANDEX_SERVER_ERROR_DIALOG = "YANDEX_SERVER_ERROR_DIALOG";

    public LastUploadedTask(ListImagesFragment listImagesFragment, int mCurrentImageIndex) {
        this.listImagesFragment = listImagesFragment;
        this.mCurrentImageIndex = mCurrentImageIndex;
    }

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
                    .addMessage(listImagesFragment.getString(R.string.there_was_a_problem_with_the_network) +
                            " (" + e.getMessage() + ")");

        } catch (ServerIOException e) {
            e.printStackTrace();
            clearPreferences();
            new YandexServerErrorDialog().show(listImagesFragment.getFragmentManager(), YANDEX_SERVER_ERROR_DIALOG);
            return new BackgroundResponse(BackgroundStatus.ERROR)
                    .addMessage(listImagesFragment.getString(R.string.there_was_a_problem_with_the_yandex_server) +
                            " (" + e.getMessage() + ")");
        }
    }

    private void clearPreferences() {
        SharedPreferences preferences = listImagesFragment.getActivity().getPreferences(MODE_PRIVATE);
        preferences.edit().clear().apply();
    }

    @Override
    protected void onPostExecute(BackgroundResponse response) {
        listImagesFragment.onGetLastUploadedImages(response);
    }
}
