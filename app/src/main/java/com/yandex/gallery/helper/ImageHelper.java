package com.yandex.gallery.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slavik on 4/17/18.
 */

public final class ImageHelper {
    private static final String LOG_TAG = "ImageHelper";
    private static BitmapFactory.Options sOptions;

    public static Point calculateDisplaySize(Fragment fragment) {
        Display display = fragment.getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    static {
        setUpOptions();
    }

    private static void setUpOptions() {
        sOptions = new BitmapFactory.Options();
        sOptions.inSampleSize = 2;
        sOptions.inPreferredConfig = Bitmap.Config.RGB_565;
    }


    public static List<Bitmap> decodeImages(List<ByteArrayOutputStream> data, Point display) {
        Log.d(LOG_TAG, " start decodeImages");

        List<Bitmap> bitmaps = new ArrayList<>();
        long start = System.currentTimeMillis();
        for (ByteArrayOutputStream byteArrayOutputStream : data) {
            try {
                BitmapRegionDecoder bitmapRegionDecoder = BitmapRegionDecoder.newInstance(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.toByteArray().length, false);

                int imageHeight = bitmapRegionDecoder.getHeight();
                int imageWidth = bitmapRegionDecoder.getWidth();

                int minSide = imageHeight > imageWidth ? imageWidth : imageHeight;
                int centerHeightImage = imageHeight / 2;
                int centerWidthImage = imageWidth / 2;

                Bitmap bitmap = bitmapRegionDecoder.decodeRegion(new Rect(centerWidthImage - minSide / 2, centerHeightImage - minSide / 2, centerWidthImage + minSide / 2, centerHeightImage + minSide / 2), sOptions);
                bitmaps.add(Bitmap.createScaledBitmap(bitmap, display.x / 2 - 3, display.x / 2, false));
            } catch (IOException e) {
                //TODO
                e.printStackTrace();
            }
        }

        Log.d(LOG_TAG, " end decodeImages, elapsed time = " + (System.currentTimeMillis() - start));

        return bitmaps;
    }
}
