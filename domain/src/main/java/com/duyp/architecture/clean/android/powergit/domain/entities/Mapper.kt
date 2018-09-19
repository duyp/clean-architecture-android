package com.duyp.architecture.clean.android.powergit.domain.entities

import android.support.annotation.NonNull
import io.reactivex.functions.Consumer
import java.util.*

/**
 * Created by duypham on 3/28/18.
 */

abstract class Mapper<E, T> {

    @NonNull abstract fun mapFrom(@NonNull e: E): T

    fun mapFrom(e: E, tConsumer: Consumer<T>?): T {
        val value = mapFrom(e)
        if (tConsumer != null) {
            try {
                tConsumer.accept(value)
            } catch (ignored: Exception) {
            }

        }
        return value
    }

    @JvmOverloads
    fun mapFrom(eList: List<E>?, tConsumer: Consumer<T>? = null): List<T> {
        val list = ArrayList<T>()
        if (eList != null) {
            for (e in eList) {
                list.add(mapFrom(e, tConsumer))
            }
        }
        return list
    }
}
