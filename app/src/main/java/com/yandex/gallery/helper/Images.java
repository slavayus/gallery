package com.yandex.gallery.helper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Store of downloaded images
 */

public class Images {
    private static final List<ByteArrayOutputStream> mImages = new ArrayList<>();

    /**
     * Add new image to store
     *
     * @param image downloaded image
     */
    public static void addImage(ByteArrayOutputStream image) {
        mImages.add(image);
    }

    /**
     * Return an image by index from store
     *
     * @param index index of an image in store
     * @return image
     */
    public static ByteArrayOutputStream getImage(int index) {
        return mImages.get(index);
    }

    /**
     * Return all images
     *
     * @return store of downloaded images
     */
    public static List<ByteArrayOutputStream> getAll() {
        return mImages;
    }
}
