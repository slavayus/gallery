package com.yandex.gallery.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.yandex.disk.rest.json.Resource;
import com.yandex.gallery.ListImagesFragment;
import com.yandex.gallery.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slavik on 4/18/18.
 */

public class DownloadPreviewImagesTask extends AsyncTask<String, Void, BackgroundResponse> {

    private final List<Resource> data;
    private final ListImagesFragment listImagesFragment;

    public DownloadPreviewImagesTask(List<Resource> data, ListImagesFragment listImagesFragment) {
        this.data = data;
        this.listImagesFragment = listImagesFragment;
    }

    @Override
    protected BackgroundResponse doInBackground(String... strings) {
        List<Bitmap> bitmaps = new ArrayList<>();
        try {
            for (Resource resource : data) {
                InputStream inputStream = new URL(resource.getPreview()).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                bitmaps.add(bitmap);
            }
            return new BackgroundResponse<List<Bitmap>>(BackgroundStatus.OK).addData(bitmaps);
        } catch (IOException e) {
            e.printStackTrace();
            return new BackgroundResponse<List<ByteArrayOutputStream>>(BackgroundStatus.ERROR)
                    .addMessage(listImagesFragment.getString(R.string.there_was_a_problem_with_the_network) +
                            " (" + e.getMessage() + ")");
        }

    }

    @Override
    protected void onPostExecute(BackgroundResponse response) {
        listImagesFragment.onDownloadImages(response);
    }
}
