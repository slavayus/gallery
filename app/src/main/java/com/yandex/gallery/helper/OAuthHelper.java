package com.yandex.gallery.helper;

/**
 * Created by slavik on 4/15/18.
 */

public final class OAuthHelper {
    private static final String OAUTH_URL = "https://oauth.yandex.ru/authorize";
    private static final String RESPONSE_TYPE = "token";
    private static final String CLIENT_ID = "466db9eb1d744a8b947de4e89551c1ee";

    private OAuthHelper() {
    }

    public static String getUri() {
        return OAUTH_URL + "?" +
                "response_type=" + RESPONSE_TYPE +
                "&client_id=" + CLIENT_ID;
    }
}
