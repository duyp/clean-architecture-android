package com.duyp.architecture.clean.android.powergit

import android.arch.lifecycle.Observer

/**
 * An [Observer] which only emits non-null content
 */
class NonNullObserver<T>(private val nonNullContent: (T) -> Unit) : Observer<T> {

    override fun onChanged(t: T?) {
        t?.let(nonNullContent)
    }

}