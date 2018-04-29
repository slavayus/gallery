package com.yandex.gallery.tasks;

/**
 * Store response from AsyncTasks
 *
 * @see android.os.AsyncTask
 */

public class BackgroundResponse<T> {
    private T data;
    private BackgroundStatus status;
    private String message;

    /**
     * Creates a new BackgroundResponse with the given status.
     *
     * @param status AsyncTask status
     * @see BackgroundStatus
     */
    BackgroundResponse(BackgroundStatus status) {
        this.status = status;
    }

    /**
     * Add a response message
     *
     * @param message response message
     * @return current BackgroundResponse
     */
    BackgroundResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * Add a response data
     *
     * @param data response data
     * @return current BackgroundResponse
     */
    BackgroundResponse setData(T data) {
        this.data = data;
        return this;
    }

    /**
     * Gets the stored data
     *
     * @return this stored data
     */
    public T getData() {
        return data;
    }

    /**
     * Gets the stored status
     *
     * @return this stored status
     * @see BackgroundStatus
     */
    public BackgroundStatus getStatus() {
        return status;
    }

    /**
     * Gets the stored message
     *
     * @return this stored message
     */
    public String getMessage() {
        return message;
    }
}
