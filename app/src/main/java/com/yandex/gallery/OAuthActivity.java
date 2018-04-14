package com.yandex.gallery;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OAuthActivity extends AppCompatActivity {
    private final String LOG_TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);

        TextView tokenTextView = findViewById(R.id.token);

        Uri data = getIntent().getData();
        if (data != null) {
            Log.d(LOG_TAG, data.toString());
            Pattern pattern = Pattern.compile("access_token=(.*?)(&|$)");
            Matcher matcher = pattern.matcher(data.toString());
            if (matcher.find()) {
                tokenTextView.setText(matcher.group(1));
            }
        } else {
            Log.d(LOG_TAG, "data is null");
        }
    }
}
