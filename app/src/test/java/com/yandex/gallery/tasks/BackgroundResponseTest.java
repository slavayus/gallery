package com.yandex.gallery.tasks;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

/**
 * Testing BackgroundResponse class
 */
public class BackgroundResponseTest {

    @Test
    public void setMessage() throws Exception {
        String THIS_IS_A_MESSAGE = "this is a message";

        BackgroundResponse<Integer> response = new BackgroundResponse<>(BackgroundStatus.OK);
        response.setMessage(THIS_IS_A_MESSAGE);

        Field message = response.getClass().getDeclaredField("message");
        message.setAccessible(true);
        assertEquals(THIS_IS_A_MESSAGE, message.get(response));

        assertNotEquals("AN ANOTHER MESSAGE", message.get(response));

        response.setMessage(null);
        assertNull(message.get(response));

    }

    @Test
    public void setData() throws Exception {
        BackgroundResponse<Integer> response = new BackgroundResponse<>(BackgroundStatus.OK);
        response.setData(4);

        Field data = response.getClass().getDeclaredField("data");
        data.setAccessible(true);
        assertEquals(4, data.get(response));

        assertNotEquals(5, data.get(response));

        response.setData(null);
        assertNull(data.get(response));
    }

    @Test
    public void getData() throws Exception {
        BackgroundResponse<Integer> response = new BackgroundResponse<>(BackgroundStatus.OK);
        Field data = response.getClass().getDeclaredField("data");
        data.setAccessible(true);

        data.set(response, 4);

        assertEquals(new Integer(4), response.getData());

        assertNotEquals(new Integer(5), response.getData());

        data.set(response, null);
        assertNull(response.getData());

    }

    @Test
    public void getStatus() throws Exception {
        BackgroundResponse<Integer> response = new BackgroundResponse<>(BackgroundStatus.OK);
        Field status = response.getClass().getDeclaredField("status");
        status.setAccessible(true);

        assertEquals(BackgroundStatus.OK, response.getStatus());

        assertNotEquals(BackgroundStatus.ERROR, response.getStatus());

        status.set(response, null);
        assertNull(response.getStatus());

        status.set(response, BackgroundStatus.ERROR);
        assertEquals(BackgroundStatus.ERROR, response.getStatus());

        response = new BackgroundResponse<>(BackgroundStatus.ERROR);
        status = response.getClass().getDeclaredField("status");
        status.setAccessible(true);

        assertNotEquals(BackgroundStatus.OK, response.getStatus());

    }

    @Test
    public void getMessage() throws Exception {
        String THIS_IS_A_MESSAGE = "this is a message";

        BackgroundResponse<Integer> response = new BackgroundResponse<>(BackgroundStatus.OK);
        Field message = response.getClass().getDeclaredField("message");
        message.setAccessible(true);

        message.set(response, THIS_IS_A_MESSAGE);

        assertEquals(THIS_IS_A_MESSAGE, response.getMessage());

        assertNotEquals("AN ANOTHER MESSAGE", response.getMessage());

        message.set(response, null);
        assertNull(response.getMessage());

    }

}