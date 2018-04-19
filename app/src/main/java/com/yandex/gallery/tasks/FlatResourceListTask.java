package com.yandex.gallery.tasks;

import android.os.AsyncTask;

import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.ResourcesArgs;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.exceptions.ServerIOException;
import com.yandex.disk.rest.json.Resource;
import com.yandex.disk.rest.json.ResourceList;
import com.yandex.gallery.ListImagesFragment;
import com.yandex.gallery.R;

import java.io.IOException;

/**
 * Created by slavik on 4/18/18.
 */

public class FlatResourceListTask extends AsyncTask<String, Void, BackgroundResponse> {
    private final ListImagesFragment listImagesFragment;
    private final int mCurrentImageIndex;

    public FlatResourceListTask(ListImagesFragment listImagesFragment, int mCurrentImageIndex) {
        this.listImagesFragment = listImagesFragment;
        this.mCurrentImageIndex = mCurrentImageIndex;
    }

    @Override
    protected BackgroundResponse doInBackground(String... data) {
        try {
            Credentials credentials = new Credentials("", data[0]);

            RestClient restClient = new RestClient(credentials);
            ResourceList resourceList = restClient.getFlatResourceList(new ResourcesArgs.Builder().setMediaType("image").setLimit(1).setOffset(mCurrentImageIndex).build());

            return new BackgroundResponse<Resource>(BackgroundStatus.OK).addData(resourceList.getItems().get(0));
        } catch (IOException e) {
            e.printStackTrace();
            return new BackgroundResponse(BackgroundStatus.ERROR)
                    .addMessage(listImagesFragment.getString(R.string.there_was_a_problem_with_the_network) +
                            " (" + e.getMessage() + ")");

        } catch (ServerIOException e) {
            e.printStackTrace();
            return new BackgroundResponse(BackgroundStatus.ERROR)
                    .addMessage(listImagesFragment.getString(R.string.there_was_a_problem_with_the_yandex_server) +
                            " (" + e.getMessage() + ")");
        }

    }

    @Override
    protected void onPostExecute(BackgroundResponse response) {
        listImagesFragment.onGetLastUploadedImages(response);
    }
}
