package com.duyp.architecture.clean.android.powergit.domain.utils.functions;


/**
 * Same as {@link io.reactivex.functions.BiConsumer} but without exception throw
 */
public interface PlainBiConsumer<T1, T2> {

    /**
     * Performs an operation on the given values.
     * @param t1 the first value
     * @param t2 the second value
     */
    void accept(T1 t1, T2 t2);
}
