package com.yandex.gallery.helper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slavik on 4/20/18.
 */

public class Images {
    private static final List<ByteArrayOutputStream> mImages = new ArrayList<>();

    public static void addImage(ByteArrayOutputStream image) {
        mImages.add(image);
    }

    public static ByteArrayOutputStream getImage(int index) {
        return mImages.get(index);
    }

    public static List<ByteArrayOutputStream> getAll() {
        return mImages;
    }
}
