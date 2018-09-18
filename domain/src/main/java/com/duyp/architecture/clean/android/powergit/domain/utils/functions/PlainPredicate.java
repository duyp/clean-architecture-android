package com.duyp.architecture.clean.android.powergit.domain.utils.functions;

/**
 * Created by duypham on 3/5/18.
 * Similar to {@link io.reactivex.functions.Predicate} but without exception throws
 */

public interface PlainPredicate<T> {

    /**
     * Test the given input value and return a boolean.
     * @param t the value
     * @return the boolean result
     */
    boolean test(T t);
}