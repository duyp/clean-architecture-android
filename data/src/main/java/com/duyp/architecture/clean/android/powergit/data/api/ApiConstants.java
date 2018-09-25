package com.duyp.architecture.clean.android.powergit.data.api;

/**
 * Created by duypham on 9/7/17.
 *
 */

public class ApiConstants {

    public static final int TIME_OUT_API = 30; // second

    // format for all timestamps returned from github apis
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public interface Headers {

        String KEY_CACHE_CONTROL = "Cache-Control";
        String KEY_ACCEPT = "Accept";
        String KEY_ACCEPT_LANGUAGE = "Accept-Language";
        String KEY_X_API_KEY = "X-IS24-Api-Key";
        String KEY_X_API_VERSION = "X-IS24-Api-Version";
        String KEY_X_UDID = "X-Udid";
        String KEY_X_REQUEST_DATE = "X-Request-Date";
        String KEY_X_TOKEN = "X-Token";
        String KEY_USER_AGENT = "User-Agent";
        String KEY_ACCEPT_ENCODING = "Accept-Encoding";
        String KEY_AUTHORIZATION = "Authorization";
        String KEY_IF_MODIFIED_SINCE = "If-Modified-Since";

    }
}
