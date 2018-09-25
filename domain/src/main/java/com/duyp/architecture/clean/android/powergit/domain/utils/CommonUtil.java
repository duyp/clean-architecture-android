package com.duyp.architecture.clean.android.powergit.domain.utils;

public final class CommonUtil {

    /**
     * check if given input is either null or zero length.
     *
     * @param str input
     *
     * @return true if input is empty, otherwise false.
     */
    public static boolean isEmpty(final CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNumeric(final String str) {
        return str.matches("[-+]?\\d*\\.?\\d+");
    }
}
