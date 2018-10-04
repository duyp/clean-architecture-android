package com.duyp.architecture.clean.android.powergit.utils

import android.arch.lifecycle.Observer
import io.reactivex.internal.functions.ObjectHelper
import io.reactivex.internal.util.ExceptionHelper

/**
 * Used for testing [android.arch.lifecycle.LiveData]
 * @param <T>
 */
class TestObserver<T> : Observer<T> {

    private val values: MutableList<T> = ArrayList()

    override fun onChanged(t: T?) {
        t?.let { values.add(it) }
    }

    /**
     * Assert that this Observer received the value at given [position] through [.onChanged] with provided [valuePredicate]
     * @param valuePredicate should return true for expected value
     */
    fun assertValue(position: Int, valuePredicate: T.() -> Boolean): OnGoingStub<T> {
        failIfNoValue()

        var found = false

        try {
            if (valuePredicate.invoke(this.values[position])) {
                found = true
            }
        } catch (ex: Exception) {
            throw ExceptionHelper.wrapOrThrow(ex)
        }

        if (!found) {
            throw fail("Value not present")
        }
        return OnGoingStub(this, position)
    }

    /**
     * Assert this observer received the last value with provied [valuePredicate]
     */
    fun assertValue(valuePredicate: T.() -> Boolean): OnGoingStub<T> {
        val position = values.size - 1
        assertValue(position, valuePredicate)
        return OnGoingStub(this, position)
    }

    /**
     * Assert that this Observer received the value at given [position] which is equal to the given value
     * @param value value to assert
     */
    fun assertValueEquals(position: Int, value: T): TestObserver<T> {
        failIfNoValue()
        if (!ObjectHelper.equals(this.values[position], value)) {
            throw fail("Value not equal, expected: $value, actual: ${this.values[position]}")
        }
        return this
    }

    /**
     * Assert that this Observer received the last value which is equal to the given value
     * @param value value to assert
     */
    fun assertValueEquals(value: T): TestObserver<T> {
        failIfNoValue()
        assertValueEquals(values.size - 1, value)
        return this
    }



    /**
     * Assert that this Observer haven't received any value yet
     */
    fun assertNoValue(): TestObserver<T> {
        if (values.isNotEmpty()) {
            throw fail("Expect no value but has")
        }
        return this
    }

    fun assertValuesCount(count: Int): TestObserver<T> {
        if (values.size != count) {
            throw fail("Expect $count values but has")
        }
        return this
    }

    fun assertValuesCount(countPredicate: (Int) -> Boolean): TestObserver<T> {
        if (!countPredicate(values.size)) {
            throw fail("Values count not present")
        }
        return this
    }

    /**
     * Fail if this observe hasn't receive any value yet
     */
    private fun failIfNoValue() {
        if (values.isEmpty()) {
            throw fail("No value present")
        }
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
                " (" + "value = \n" + printValues() + ')'.toString()

        return AssertionError(b)
    }

    private fun printValues(): String {
        val sb = StringBuilder()
        values.forEach { sb.append(it).append("\n") }
        return sb.toString()
    }

    class OnGoingStub<T> (
            private val testObserver: TestObserver<T>,
            private val currentPosition: Int
    ) {

        fun withPrevious(valuePredicate: T.() -> Boolean): OnGoingStub<T> {
            return testObserver.assertValue(currentPosition - 1, valuePredicate)
        }

        fun withNext(valuePredicate: T.() -> Boolean): OnGoingStub<T> {
            return testObserver.assertValue(currentPosition + 1, valuePredicate)
        }

        fun noPrevious(): TestObserver<T> {
            return testObserver.assertValuesCount { it == 1 }
        }

        fun noNext(): TestObserver<T> {
            return testObserver.assertValuesCount { currentPosition == it - 1 }
        }

    }

}
