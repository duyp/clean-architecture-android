package com.duyp.architecture.clean.android.powergit.domain.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duyp.architecture.clean.android.powergit.domain.utils.functions.PlainFunction;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class CommonUtil {

    @NonNull public static String join(@NonNull final CharSequence delimiter, @NonNull final Iterable tokens) {
        final StringBuilder sb = new StringBuilder();
        final Iterator<?> it = tokens.iterator();
        if(it.hasNext()) {
            sb.append(it.next());
            while(it.hasNext()) {
                sb.append(delimiter);
                sb.append(it.next());
            }
        }
        return sb.toString();
    }

    public static <T> List<T> split(final String delimiter, @NonNull final String list,
                                    @NonNull final PlainFunction<String, T> func) {
        final String[] array = list.split(delimiter);
        final List<T> result = new ArrayList<>();
        for(final String s : array) {
            result.add(func.apply(s));
        }
        return result;
    }

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

    public static String toHMACMD5String(@Nullable final String message, final String keyString) {
        if (message == null) return null;
        final String sEncodedString;
        final byte[] hashBytes = toHMACMD5(message, keyString);
        final StringBuffer hash = new StringBuffer();

        for (int i = 0; i < hashBytes.length; i++) {
            final String hex = Integer.toHexString(0xFF & hashBytes[i]);
            if (hex.length() == 1) {
                hash.append('0');
            }
            hash.append(hex);
        }
        sEncodedString = hash.toString();

        return sEncodedString;
    }

    /***
     * Encrypts a message with a key to a HMAC MD5 byte array.
     *
     * @param message   The message to encrypt.
     * @param keyString The key.
     * @return The encrypted message.
     */
    public static byte[] toHMACMD5(@NonNull final String message, final String keyString) {
        try {
            final Mac mac = Mac.getInstance("HMACMD5");
            final SecretKeySpec key = new SecretKeySpec((keyString).getBytes(),
                    mac.getAlgorithm());
            mac.init(key);

            return mac.doFinal(message.getBytes());
        } catch(final Exception e) {
            return new byte[0];
        }
    }

    /**
     * validate if given email string is a proper email address.
     *
     * @param email email address
     *
     * @return true if valid email address, otherwise false.
     */
    public static boolean isEmail(final String email) {
        if(isEmpty(email))
            return false;

        final Pattern p = Pattern.compile( // copy from android.util.Patterns.EMAIL_ADDRESS
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+");
        return p.matcher(email).matches();
    }

    /**
     * validate if given phone string is a proper phone number.
     *
     * @param phone phone number
     *
     * @return true if valid phone number, ohterwise false.
     */
    public static boolean isPhone(final String phone) {
        if(!isEmpty(phone)) {
            int count = 0;
            for(int i = 0; i < phone.length(); i++) {
                if(Character.isDigit(phone.charAt(i))) {
                    count++;
                }
            }
            if(count > 6 && count <= 20)
                return true;
        }

        return false;
    }

    /**
     * Describe an object by its fields (name - value)
     * @param object given object
     * @return object description
     */
    public static String describeObject(@NonNull final Object object) {
        final StringBuilder sb = new StringBuilder();
        final Class<?> clazz = object.getClass();
        final Field[] fields = clazz.getFields();
        for(final Field f : fields) {
            try {
                final String name = f.getName();
                final Object value = f.get(object);
                sb.append(" - ").append(name).append(": ").append(value).append("\n");
            } catch(final IllegalAccessException e) {
                return "";
            }
        }
        return sb.toString();
    }

    /**
     * indicates if the current version is older (lower) than the given version.
     *
     * @param minVersion     the minimum version array.
     * @param currentVersion the current version array.
     * @return true if currentVersion is lower than the minVersion.
     */
    public static boolean isCurrentVersionOlder(final int[] minVersion,
                                                final int[] currentVersion) {

        final int currMajor = currentVersion[0];
        final int currMinor = currentVersion[1];
        final int currBugfix = currentVersion[2];

        final int minMajor = minVersion[0];
        final int minMinor = minVersion[1];
        final int minBugfix = minVersion[2];

        if (currMajor == minMajor) {
            if (currMinor == minMinor) {
                if (currBugfix < minBugfix)
                    return true;
                else
                    return false;
            } else if (currMinor < minMinor)
                return true;
            else
                return false;
        } else if (currMajor < minMajor)
            return true;
        else
            return false;
    }

    /**
     * indicates if the current version is newer (higher) than the given
     * version.
     *
     * @param maxVersion     the maximum version array.
     * @param currentVersion the current version array.
     * @return true if currentVersion is higher than the maxVersion.
     */
    public static boolean isCurrentVersionNewer(final int[] maxVersion,
                                                final int[] currentVersion) {

        final int currMajor = currentVersion[0];
        final int currMinor = currentVersion[1];
        final int currBugfix = currentVersion[2];

        final int maxMajor = maxVersion[0];
        final int maxMinor = maxVersion[1];
        final int maxBugfix = maxVersion[2];

        if (currMajor == maxMajor) {
            if (currMinor == maxMinor) {
                if (currBugfix > maxBugfix)
                    return true;
                else
                    return false;
            } else if (currMinor > maxMinor)
                return true;
            else
                return false;
        } else if (currMajor > maxMajor)
            return true;
        else
            return false;
    }

    /**
     * gets version string separated into major, minor and bug fix version.
     *
     * @param versionString the version string like "2.2.0"
     * @return the version as array (ex. [2,2,0]).
     */
    public static int[] getVersionParts(final String versionString) {
        final int[] version = new int[]{0, 0, 0};
        try {
            final String[] versionParts = versionString.split("\\.", -1);
            if (versionParts.length == 3) {
                for (int i = 0; i < versionParts.length; i++) {
                    version[i] = Integer.valueOf(versionParts[i]);
                }
            }
        } catch (final NumberFormatException ex) {
            return new int[]{0, 0, 0};
        }

        return version;
    }
}
