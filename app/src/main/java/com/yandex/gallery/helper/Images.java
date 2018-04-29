package com.yandex.gallery.helper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Store of downloaded images.
 * Singleton class for storing.
 */

public final class Images {
    private static Images images;
    private final List<ByteArrayOutputStream> mImages = new ArrayList<>();

    private Images() {
    }

    /**
     * Single instance
     *
     * @return the instance on this class
     */
    public static Images instance() {
        if (images == null) {
            images = new Images();
        }
        return images;
    }

    /**
     * Add new image to store
     *
     * @param image downloaded image
     */
    public void addImage(ByteArrayOutputStream image) {
        mImages.add(image);
    }

    /**
     * Return an image by index from store
     *
     * @param index index of an image in store
     * @return image
     */
    public ByteArrayOutputStream getImage(int index) {
        return mImages.get(index);
    }

    /**
     * Return all images
     *
     * @return store of downloaded images
     */
    public List<ByteArrayOutputStream> getAll() {
        return mImages;
    }
}
