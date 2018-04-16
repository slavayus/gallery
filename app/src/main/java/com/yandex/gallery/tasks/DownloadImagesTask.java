package com.yandex.gallery.tasks;

import android.os.AsyncTask;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slavik on 4/16/18.
 */

public class DownloadImagesTask extends AsyncTask<String, Void, BackgroundResponse> {
    private final List<Resource> resources;
    private final ListImagesFragment listImagesFragment;

    public DownloadImagesTask(List<Resource> resources, ListImagesFragment listImagesFragment) {
        this.listImagesFragment = listImagesFragment;
        this.resources = resources;
    }

    @Override
    protected BackgroundResponse doInBackground(String... data) {
        final List<ByteArrayOutputStream> outputStreams = new ArrayList<>();
        try {
            Credentials credentials = new Credentials("", data[0]);

            RestClient restClient = new RestClient(credentials);
            for (Resource resource : resources) {
                restClient.downloadFile(resource.getPath().getPath(), new DownloadListener() {
                    @Override
                    public OutputStream getOutputStream(boolean append) throws IOException {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        outputStreams.add(byteArrayOutputStream);
                        return byteArrayOutputStream;
                    }
                });
            }
            return new BackgroundResponse<List<ByteArrayOutputStream>>(BackgroundStatus.OK).addData(outputStreams);
        } catch (IOException e) {
            e.printStackTrace();
            return new BackgroundResponse<List<ByteArrayOutputStream>>(BackgroundStatus.OK)
                    .addMessage(listImagesFragment.getString(R.string.there_was_a_problem_with_the_network) +
                            " (" + e.getMessage() + ")");
        } catch (ServerException e) {
            e.printStackTrace();
            return new BackgroundResponse<List<ByteArrayOutputStream>>(BackgroundStatus.OK)
                    .addMessage(listImagesFragment.getString(R.string.there_was_a_problem_with_the_yandex_server) +
                            " (" + e.getMessage() + ")");
        }
    }


    @Override
    protected void onPostExecute(BackgroundResponse response) {
        listImagesFragment.onDownloadImages(response);
    }
}
