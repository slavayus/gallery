package com.yandex.gallery.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.yandex.gallery.ListImagesFragment;
import com.yandex.gallery.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by slavik on 4/17/18.
 */

public final class ImageHelper {
    private static final String LOG_TAG = "ImageHelper";
    private static BitmapFactory.Options sOptions;


    static {
        setUpOptions();
    }

    private static void setUpOptions() {
        sOptions = new BitmapFactory.Options();
        sOptions.inSampleSize = 2;
        sOptions.inPreferredConfig = Bitmap.Config.RGB_565;
    }


    public static Bitmap decodeImages(ByteArrayOutputStream data, Point display) {
        Log.d(LOG_TAG, " start decodeImages");
        Bitmap bitmap = null;
        long start = System.currentTimeMillis();
        try {
            BitmapRegionDecoder bitmapRegionDecoder = BitmapRegionDecoder.newInstance(data.toByteArray(), 0, data.toByteArray().length, false);

            int imageHeight = bitmapRegionDecoder.getHeight();
            int imageWidth = bitmapRegionDecoder.getWidth();

            int minSide = imageHeight > imageWidth ? imageWidth : imageHeight;
            int centerHeightImage = imageHeight / 2;
            int centerWidthImage = imageWidth / 2;

            bitmap = bitmapRegionDecoder.decodeRegion(new Rect(centerWidthImage - minSide / 2, centerHeightImage - minSide / 2, centerWidthImage + minSide / 2, centerHeightImage + minSide / 2), sOptions);
            bitmap = Bitmap.createScaledBitmap(bitmap, display.x / 2 - 3, display.x / 2, false);
            bitmap = getRoundedCornerBitmap(bitmap);
        } catch (IOException e) {
            //TODO
            e.printStackTrace();
        }

        Log.d(LOG_TAG, " end decodeImages, elapsed time = " + (System.currentTimeMillis() - start));

        return bitmap;
    }


    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    public static Bitmap createEmptyImage(Point mDisplay, ListImagesFragment listImagesFragment) {
        Bitmap bitmap = Bitmap.createBitmap(mDisplay.x / 2 - 3, mDisplay.x / 2, Bitmap.Config.RGB_565);
        bitmap.eraseColor(listImagesFragment.getResources().getColor(R.color.backgroundColorEmptyImage));

        return getRoundedCornerBitmap(bitmap);
    }
}
