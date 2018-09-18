package com.duyp.architecture.clean.android.powergit.domain.utils.functions;


/**
 * Similar to {@link io.reactivex.functions.Consumer} but without exception throw
 * @param <T>
 */
public interface PlainConsumer<T> {

    void accept(T t);
}
