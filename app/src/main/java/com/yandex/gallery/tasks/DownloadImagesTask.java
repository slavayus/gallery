package com.yandex.gallery.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.DownloadListener;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.exceptions.ServerException;
import com.yandex.disk.rest.json.Resource;
import com.yandex.gallery.ListImagesFragment;
import com.yandex.gallery.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by slavik on 4/16/18.
 */

public class DownloadImagesTask extends AsyncTask<String, Void, BackgroundResponse> {
    private static final String LOG_TAG = "DownloadImagesTask";

    private final Resource resource;
    private final ListImagesFragment listImagesFragment;

    private OutputStream outputStream;

    public DownloadImagesTask(Resource resources, ListImagesFragment listImagesFragment) {
        this.listImagesFragment = listImagesFragment;
        this.resource = resources;
    }

    @Override
    protected BackgroundResponse doInBackground(String... data) {
        Log.d(LOG_TAG, "start doInBackground");
        long start = System.currentTimeMillis();
        try {
            Credentials credentials = new Credentials("", data[0]);

            RestClient restClient = new RestClient(credentials);
            int i = 0;
            Log.d(LOG_TAG, "downloading image");
            restClient.downloadFile(resource.getPath().getPath(), new DownloadListener() {
                @Override
                public OutputStream getOutputStream(boolean append) throws IOException {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    outputStream = byteArrayOutputStream;
                    return byteArrayOutputStream;
                }
            });

            return new BackgroundResponse<OutputStream>(BackgroundStatus.OK).addData(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return new BackgroundResponse<OutputStream>(BackgroundStatus.ERROR)
                    .addMessage(listImagesFragment.getString(R.string.there_was_a_problem_with_the_network) +
                            " (" + e.getMessage() + ")");
        } catch (ServerException e) {
            e.printStackTrace();
            return new BackgroundResponse<OutputStream>(BackgroundStatus.ERROR)
                    .addMessage(listImagesFragment.getString(R.string.there_was_a_problem_with_the_yandex_server) +
                            " (" + e.getMessage() + ")");
        } finally {
            Log.d(LOG_TAG, "end doInBackground, elapsed time = " + (System.currentTimeMillis() - start));
        }
    }


    @Override
    protected void onPostExecute(BackgroundResponse response) {
        Log.d(LOG_TAG, "start onPostExecute");
        listImagesFragment.onDownloadImages(response);
    }
}
