package com.yandex.gallery.listeners;

import com.yandex.gallery.tasks.BackgroundResponse;

/**
 * Listener for downloaded images
 */

public interface DownloadImageListener {
    void onDownloadImages(BackgroundResponse response);
}
