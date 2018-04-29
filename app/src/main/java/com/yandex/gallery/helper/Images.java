package com.yandex.gallery.helper;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Store of downloaded images.
 * Singleton class for storing.
 */

public final class Images {
    private static Images sInstance;
    private final List<OutputStream> mImages = new ArrayList<>();

    private Images() {
    }

    /**
     * Single instance
     *
     * @return the instance on this class
     */
    public static Images instance() {
        if (sInstance == null) {
            sInstance = new Images();
        }
        return sInstance;
    }

    /**
     * Add new image to store
     *
     * @param image downloaded image
     */
    public void addImage(OutputStream image) {
        mImages.add(image);
    }

    /**
     * Return an image by index from store
     *
     * @param index index of an image in store
     * @return image
     */
    public OutputStream getImage(int index) {
        return mImages.get(index);
    }

    /**
     * Return all images
     *
     * @return store of downloaded images
     */
    public List<OutputStream> getAll() {
        return mImages;
    }
}
