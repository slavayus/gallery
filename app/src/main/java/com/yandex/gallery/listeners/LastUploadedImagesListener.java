package com.yandex.gallery.listeners;

import com.yandex.gallery.tasks.BackgroundResponse;

/**
 * Listener for last uploaded images
 */

public interface LastUploadedImagesListener {
    void onGetLastUploadedImages(BackgroundResponse response);
}
