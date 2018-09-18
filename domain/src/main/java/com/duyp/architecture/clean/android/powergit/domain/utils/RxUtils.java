package com.duyp.architecture.clean.android.powergit.domain.utils;

import com.duyp.architecture.clean.android.powergit.domain.utils.functions.PlainFunction;
import com.duyp.architecture.clean.android.powergit.domain.utils.functions.PlainPredicate;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by Duy Pham on 7/31/17.
 * Utilities functions for functional reactive programming
 * http://blog.danlew.net/2017/07/27/an-introduction-to-functional-reactive-programming/
 */
public class RxUtils {

    /**
     * Map an input list to another output list by applying given {@link PlainFunction} on each item of the input list
     * @param input input List
     * @param func function to apply on each item of input list
     * @param <T> Input Type
     * @param <R> Return Type
     * @return mapped list
     */
    @NonNull
    public static <T, R> List<R> map(@NonNull Iterable<T> input, @NonNull PlainFunction<T, R> func) {
        List<R> output = new ArrayList<>();
        for (T t : input) {
            output.add(func.apply(t));
        }
        return output;
    }

    /**
     * Filter a list by a given {@link PlainPredicate}
     * @param input input list
     * @param predicate predicate to filter
     * @param <T> input Type
     * @return filtered list
     */
    @NonNull
    public static <T> List<T> filter(@NonNull Iterable<T> input, @NonNull PlainPredicate<T> predicate) {
        List<T> out = new ArrayList<>();
        for (T t : input) {
            if (predicate.test(t)) {
                out.add(t);
            }
        }
        return out;
    }

    /**
     * Filter an array by a given {@link PlainPredicate}
     * @param input input list
     * @param predicate predicate to filter
     * @param <T> input Type
     * @return filtered list
     */
    @NonNull
    public static <T> List<T> filter(@NonNull T[] input, @NonNull PlainPredicate<T> predicate) {
        List<T> out = new ArrayList<>();
        for (T t : input) {
            if (predicate.test(t)) {
                out.add(t);
            }
        }
        return out;
    }

    @NonNull
    public static <T> boolean[] filterIndex(@NonNull List<T> input, @NonNull PlainPredicate<T> predicate) {
        boolean[] result = new boolean[input.size()];
        int i = 0;
        for (T t : input) {
            result[i++] = predicate.test(t);
        }
        return result;
    }
}
