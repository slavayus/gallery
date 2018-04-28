package com.yandex.gallery.tasks;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.DownloadListener;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.exceptions.ServerException;
import com.yandex.disk.rest.json.Resource;
import com.yandex.gallery.R;
import com.yandex.gallery.listeners.DownloadImageListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Download image from yandex disk by URI
 *
 * @see BackgroundResponse
 */

public class DownloadImagesTask extends AsyncTask<String, Void, BackgroundResponse> {
    private static final String LOG_TAG = "DownloadImagesTask";

    private final Resource resource;
    private final Fragment fragment;

    private OutputStream outputStream;

    /**
     * Creates a new AsyncTask to download image by resources
     *
     * @param resources resources with URI to downloading image
     * @param fragment  instance of Fragment
     * @see Resource
     */
    public DownloadImagesTask(Resource resources, Fragment fragment) {
        this.fragment = fragment;
        this.resource = resources;
    }

    /**
     * Download image
     *
     * @param data 0Auth token for authorization
     * @return download status
     */
    @Override
    protected BackgroundResponse doInBackground(String... data) {
        Log.d(LOG_TAG, "start doInBackground");
        long start = System.currentTimeMillis();
        try {
            Credentials credentials = new Credentials("", data[0]);

            RestClient restClient = new RestClient(credentials);
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
                    .addMessage(fragment.getString(R.string.network_error_text));
        } catch (ServerException e) {
            e.printStackTrace();
            return new BackgroundResponse<OutputStream>(BackgroundStatus.ERROR)
                    .addMessage(fragment.getString(R.string.yandex_server_error_text));
        } finally {
            Log.d(LOG_TAG, "end doInBackground, elapsed time = " + (System.currentTimeMillis() - start));
        }
    }


    @Override
    protected void onPostExecute(BackgroundResponse response) {
        Log.d(LOG_TAG, "start onPostExecute");
        ((DownloadImageListener) fragment).onDownloadImages(response);
        Log.d(LOG_TAG, "end onPostExecute");
    }
}
