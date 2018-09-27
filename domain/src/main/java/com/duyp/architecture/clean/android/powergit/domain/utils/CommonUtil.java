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

    /**
     * Check if at least 1 element of given arrays is empty
     *
     * @return true if at least 1 element is empty
     */
    public static boolean isEmpty(final CharSequence... str) {
        for(CharSequence s : str) {
            if (isEmpty(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNumeric(final String str) {
        return str.matches("[-+]?\\d*\\.?\\d+");
    }
}
