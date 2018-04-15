package com.yandex.gallery.helper;

/**
 * Created by slavik on 4/15/18.
 */

public final class OAuthHelper {
    private static final String oAuthUrl = "https://oauth.yandex.ru/authorize";
    private static final String responseType = "token";
    private static final String clientId = "466db9eb1d744a8b947de4e89551c1ee";

    private OAuthHelper() {
    }

    public static String getUri() {
        return oAuthUrl + "?" +
                "response_type=" + responseType +
                "&client_id=" + clientId;
    }
}
