package com.duyp.architecture.clean.android.powergit.domain.utils.functions;

/**
 * Similar to {@link java.util.function.Function} but no exception thrown
 */
public interface PlainFunction<T, R> {

    R apply(T t);
}
