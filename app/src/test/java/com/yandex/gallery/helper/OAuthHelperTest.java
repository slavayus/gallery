package com.yandex.gallery.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test class for OAuthHelper
 */
public class OAuthHelperTest {
    @Test
    public void getUri() throws Exception {
        assertEquals("https://oauth.yandex.ru/authorize?response_type=token&client_id=466db9eb1d744a8b947de4e89551c1ee", OAuthHelper.getUri());
        assertNotEquals(null, OAuthHelper.getUri());
    }

}