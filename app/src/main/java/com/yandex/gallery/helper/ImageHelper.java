package com.yandex.gallery.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.yandex.gallery.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Class helper for working with Bitmap
 */

public final class ImageHelper {
    private static final String LOG_TAG = "ImageHelper";
    private static BitmapFactory.Options sOptions;


    static {
        setUpOptions();
    }

    /**
     * Set up options with which image will be encoded
     *
     * @see ImageHelper#decodeImage(OutputStream)
     */
    private static void setUpOptions() {
        sOptions = new BitmapFactory.Options();
        sOptions.inSampleSize = 2;
        sOptions.inPreferredConfig = Bitmap.Config.RGB_565;
    }


    /**
     * Decode image in the form of a square.
     * Divides the screen in two and make a square from image with this params.
     *
     * @param data     output stream with image
     * @param display  display params
     * @param activity current activity
     * @return Bitmap with decoded image.
     * @see Point
     */
    public static Bitmap decodeImageRegion(ByteArrayOutputStream data, Point display, Activity activity) {
        Log.d(LOG_TAG, " start decodeImageRegion");
        Bitmap bitmap;
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
            e.printStackTrace();
            return createEmptyImage(display, activity.getResources().getColor(R.color.emptyImageColor));
        }

        Log.d(LOG_TAG, " end decodeImages, elapsed time = " + (System.currentTimeMillis() - start));

        return bitmap;
    }


    /**
     * Decode full image
     *
     * @param image output stream with image
     * @return Bitmap with decoded image.
     */
    public static Bitmap decodeImage(OutputStream image) {
        //TODO:  check instance of image
        byte[] data = ((ByteArrayOutputStream) image).toByteArray();
        return BitmapFactory.decodeByteArray(data, 0, data.length, sOptions);
    }


    /**
     * Rotate image according to degrees
     *
     * @param image   Bitmap image which will be rotated
     * @param degrees degrees to rotate the image
     * @return rotated bitmap
     */
    public static Bitmap rotateImage(Bitmap image, int degrees) {
        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);

        return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
    }


    /**
     * Make image with rounded corners
     *
     * @param bitmap an image which corners will be rounded
     * @return Bitmap which corners was rounded
     */
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


    /**
     * Create an empty bitmap. Can display this image while real image is downloading.
     *
     * @param display device display params
     * @param colorID color to fill the image
     * @return an empty bitmap with given color
     */
    public static Bitmap createEmptyImage(Point display, int colorID) {
        Bitmap bitmap = Bitmap.createBitmap(display.x / 2 - 3, display.x / 2, Bitmap.Config.RGB_565);
        bitmap.eraseColor(colorID);

        return getRoundedCornerBitmap(bitmap);
    }
}
