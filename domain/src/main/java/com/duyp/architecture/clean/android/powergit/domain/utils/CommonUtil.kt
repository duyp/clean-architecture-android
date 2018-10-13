package com.duyp.architecture.clean.android.powergit.domain.utils

class CommonUtil {

    companion object {
        /**
         * check if given input is either null or zero length.
         *
         * @param str input
         *
         * @return true if input is empty, otherwise false.
         */
        fun isEmpty(str: String?): Boolean {
            return str == null || str.isEmpty()
        }

        /**
         * Check if at least 1 element of given arrays is empty
         *
         * @return true if at least 1 element is empty
         */
        fun isEmpty(vararg str: String?): Boolean {
            for (s in str) {
                if (s == null || s.isEmpty()) {
                    return true
                }
            }
            return false
        }

        fun isNumeric(str: String): Boolean {
            return str.matches("[-+]?\\d*\\.?\\d+".toRegex())
        }
    }

}
