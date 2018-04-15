package com.yandex.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.DownloadListener;
import com.yandex.disk.rest.ResourcesArgs;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.exceptions.ServerException;
import com.yandex.disk.rest.exceptions.ServerIOException;
import com.yandex.disk.rest.json.DiskInfo;
import com.yandex.disk.rest.json.ResourceList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OAuthActivity extends AppCompatActivity {
    private final String LOG_TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);

        Uri data = getIntent().getData();
        if (data != null) {
            Log.d(LOG_TAG, data.toString());
            Pattern pattern = Pattern.compile("access_token=(.*?)(&|$)");
            Matcher matcher = pattern.matcher(data.toString());
            if (matcher.find()) {
                String token = matcher.group(1);
                TextView tokenTextView = findViewById(R.id.token);
                tokenTextView.setText(token);

                TextView diskInfoTextView = findViewById(R.id.disk_info);
                new DiskInfoTask(diskInfoTextView).execute(token);

                new LastUploadedTask().execute(token);
            }
        } else {
            Log.d(LOG_TAG, "data is null");
        }
    }


    private class DiskInfoTask extends AsyncTask<String, Void, String> {

        private TextView diskInfoTextView;

        DiskInfoTask(TextView diskInfoTextView) {
            this.diskInfoTextView = diskInfoTextView;
        }

        @Override
        protected String doInBackground(String... data) {
            try {
                Credentials credentials = new Credentials("", data[0]);
                RestClient restClient = new RestClient(credentials);
                DiskInfo diskInfo = restClient.getDiskInfo();
                return diskInfo.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return getString(R.string.there_was_a_problem_with_the_network);
            } catch (ServerIOException e) {
                e.printStackTrace();
                return getString(R.string.there_was_a_problem_with_the_yandex_server);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            diskInfoTextView.setText(s);
        }
    }

    private class LastUploadedTask extends AsyncTask<String, Void, String> {

        private Credentials credentials;

        @Override
        protected String doInBackground(String... data) {
            try {
                this.credentials = new Credentials("", data[0]);

                RestClient restClient = new RestClient(credentials);
                ResourceList resourceList = restClient.getLastUploadedResources(new ResourcesArgs.Builder().setMediaType("image").build());
                return resourceList.getItems().get(0).getPath().getPath();
            } catch (IOException e) {
                e.printStackTrace();
                return getString(R.string.there_was_a_problem_with_the_network);
            } catch (ServerIOException e) {
                e.printStackTrace();
                return getString(R.string.there_was_a_problem_with_the_yandex_server);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                ((TextView) findViewById(R.id.last_upload)).setText(s);
                new ViewImageTask(credentials).execute(s);
            }
        }
    }


    private class ViewImageTask extends AsyncTask<String, Void, String> {

        private final Credentials credentials;
        private ByteArrayOutputStream byteArrayOutputStream;

        ViewImageTask(Credentials credentials) {
            this.credentials = credentials;
        }

        @Override
        protected String doInBackground(final String... data) {
            try {
                RestClient restClient = new RestClient(credentials);
                restClient.downloadFile(data[0], new DownloadListener() {
                    @Override
                    public OutputStream getOutputStream(boolean append) throws IOException {
                        return byteArrayOutputStream = new ByteArrayOutputStream();
                    }
                });
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return getString(R.string.there_was_a_problem_with_the_network);
            } catch (ServerException e) {
                e.printStackTrace();
                return getString(R.string.there_was_a_problem_with_the_yandex_server);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            ImageView imageView = findViewById(R.id.imageView);
            if (s == null) {
                Bitmap bmp = BitmapFactory.decodeStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                imageView.setImageBitmap(bmp);
            } else {
                imageView.setContentDescription(s);
            }
        }
    }
}
