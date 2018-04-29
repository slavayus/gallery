package com.yandex.gallery.helper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

/**
 * Testing Images class
 */
public class ImagesTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * Test for method {@link Images#addImage(OutputStream)}
     *
     * @throws Exception if cant find field
     */
    @Test
    public void addImage() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Images.instance().addImage(outputStream);

        Field images = Images.instance().getClass().getDeclaredField("mImages");
        images.setAccessible(true);
        ArrayList<ByteArrayOutputStream> expected = new ArrayList<>();
        expected.add(outputStream);

        List<OutputStream> actual = null;
        try {
            actual = (List<OutputStream>) images.get(Images.instance());
            assertArrayEquals(expected.toArray(), actual.toArray());
        } catch (Exception e) {
            fail(e.getMessage());
        }

        ByteArrayOutputStream otherOutputStream = new ByteArrayOutputStream();
        expected.add(otherOutputStream);

        if (Arrays.deepEquals(expected.toArray(), actual.toArray())) {
            System.out.println(expected.size() + "   " + actual.size());
            fail("expected : " + expected + "; actual : " + actual);
        }

        Images.instance().addImage(otherOutputStream);
        try {
            actual = (List<OutputStream>) images.get(Images.instance());
            assertArrayEquals(expected.toArray(), actual.toArray());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test for method {@link Images#getImage(int)}
     *
     * @throws Exception if cant find field
     */
    @Test
    public void getImage() throws Exception {
        Field images = Images.instance().getClass().getDeclaredField("mImages");
        images.setAccessible(true);

        images.set(Images.instance(), new ArrayList<>());

        exception.expect(IndexOutOfBoundsException.class);
        Images.instance().getImage(1);
        Images.instance().getImage(0);

        ByteArrayOutputStream firstOutputStream = new ByteArrayOutputStream();

        ArrayList<ByteArrayOutputStream> expected = new ArrayList<>();
        expected.add(firstOutputStream);
        images.set(Images.instance(), expected);

        assertEquals(firstOutputStream, Images.instance().getImage(0));
        assertNotEquals(new ByteArrayOutputStream(), Images.instance().getImage(0));
    }

    /**
     * Test for method {@link Images#getAll()}
     *
     * @throws Exception if cant find field
     */
    @Test
    public void getAll() throws Exception {
        Field images = Images.instance().getClass().getDeclaredField("mImages");
        images.setAccessible(true);

        images.set(Images.instance(), new ArrayList<>());

        List<OutputStream> expected = new ArrayList<>();
        assertArrayEquals(expected.toArray(), Images.instance().getAll().toArray());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        expected.add(outputStream);

        List<OutputStream> toImagesClass = new ArrayList<>();
        toImagesClass.add(outputStream);
        images.set(Images.instance(), toImagesClass);

        assertArrayEquals(expected.toArray(), Images.instance().getAll().toArray());


        ByteArrayOutputStream otherOutputStream = new ByteArrayOutputStream();
        expected.add(otherOutputStream);

        if (Arrays.deepEquals(expected.toArray(), Images.instance().getAll().toArray())) {
            fail("Arrays are equals");
        }

        toImagesClass.add(otherOutputStream);
        images.set(Images.instance(), toImagesClass);

        assertArrayEquals(expected.toArray(), Images.instance().getAll().toArray());
    }
}