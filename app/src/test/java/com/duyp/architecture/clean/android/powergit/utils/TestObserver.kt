package com.duyp.architecture.clean.android.powergit.utils

import android.arch.lifecycle.Observer
import io.reactivex.internal.functions.ObjectHelper
import io.reactivex.internal.util.ExceptionHelper

/**
 * Used for testing [android.arch.lifecycle.LiveData]
 * @param <T>
 */
class TestObserver<T> : Observer<T> {

    private var value: T? = null

    private var valueChanged = false

    override fun onChanged(t: T?) {
        value = t
        valueChanged = true
    }

    /**
     * Assert that this Observer received the last value through [.onChanged] which provided predicate
     * returns true
     * @param valuePredicate should return true for expected value
     */
    fun assertValue(valuePredicate: T.() -> Boolean) {
        if (value == null) {
            throw fail("No value present")
        }
        var found = false

        try {
            if (valuePredicate.invoke(this.value!!)) {
                found = true
            }
        } catch (ex: Exception) {
            throw ExceptionHelper.wrapOrThrow(ex)
        }

        if (!found) {
            throw fail("Value not present")
        }
        resetAssertion()
    }

    /**
     * Assert that this Observer received the last value which is equal to the given value
     * @param value value to assert
     */
    fun assertValueEquals(value: T) {
        if (!ObjectHelper.equals(this.value, value)) {
            throw fail("Value not equal, expected: $value, actual: ${this.value}")
        }
        resetAssertion()
    }

    /**
     * Assert that this Observer haven't received any value yet
     */
    fun assertNoValue() {
        if (value != null) {
            throw fail("Expect no value but has")
        }
        resetAssertion()
    }

    /**
     * Assert that this Observer haven't received any new value since last value received through [.onChanged]
     */
    fun assertValueNotChanged() {
        if (valueChanged) {
            throw fail("Value has been changed")
        }
        resetAssertion()
    }

    /**
     * Fail with the given message and add the sequence of errors as suppressed ones.
     *
     * Note this is deliberately the only fail method. Most of the times an assertion
     * would fail but it is possible it was due to an exception somewhere. This construct
     * will capture those potential errors and report it along with the original failure.
     *
     * @param message the message to use
     * @return AssertionError the prepared AssertionError instance
     */
    private fun fail(message: String): AssertionError {
        val b = message +
                " (" + "value = " + value + ')'.toString()

        return AssertionError(b)
    }

    private fun resetAssertion() {
        valueChanged = false
    }

}
