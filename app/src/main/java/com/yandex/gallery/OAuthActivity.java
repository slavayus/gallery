package com.yandex.gallery;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.ResourcesArgs;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.exceptions.ServerIOException;
import com.yandex.disk.rest.json.DiskInfo;
import com.yandex.disk.rest.json.Resource;
import com.yandex.disk.rest.json.ResourceList;

import java.io.IOException;
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

                TextView lastUploadTextView = findViewById(R.id.last_upload);
                new LastUploadedTask(lastUploadTextView).execute(token);
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

        private TextView diskInfoTextView;

        LastUploadedTask(TextView diskInfoTextView) {
            this.diskInfoTextView = diskInfoTextView;
        }

        @Override
        protected String doInBackground(String... data) {
            try {
                Credentials credentials = new Credentials("", data[0]);
                RestClient restClient = new RestClient(credentials);
                ResourceList resourceList = restClient.getLastUploadedResources(new ResourcesArgs.Builder().setMediaType("image").build());

                StringBuilder names = new StringBuilder();
                for (Resource resource : resourceList.getItems()) {
                    names.append(resource.getName()).append("\n");
                }

                return names.toString();
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
}
