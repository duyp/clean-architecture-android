package com.duyp.architecture.clean.android.powergit.ui.features.test

import android.arch.lifecycle.Observer

/**
 * Used for testing [androidx.lifecycle.LiveData]
 *
 * @param <T> type of data
 */
@Suppress("unused")
class LiveDataTestObserver<T> : Observer<T> {

    private val values: MutableList<T> = ArrayList()

    private var currentAssertingPosition: Int? = null

    override fun onChanged(t: T?) {
        t?.let { values.add(t) }
    }

    /**
     * Assert that this Observer received the value at given [position] through [onChanged] with provided [valuePredicate]
     *
     * @param valuePredicate should return true if value is expectation
     */
    fun assertValueAt(position: Int, valuePredicate: T.() -> Boolean): LiveDataTestObserver<T> {
            failIfNoValue()

            if (position >= values.size) {
                throw fail("Invalid index $position")
            }

            if (!valuePredicate.invoke(this.values[position])) {
                throw fail("Value not present at position $position")
            }

            currentAssertingPosition = position
            return this
    }

    /**
     * Assert this observer received the last value with provided [valuePredicate]
     *
     * @param valuePredicate should return true if value is expectation
     */
    fun assertLastValue(valuePredicate: T.() -> Boolean): LiveDataTestObserver<T> {
            return assertValueAt(values.size - 1, valuePredicate)
    }

    /**
     * Assert this observer received the first value with provided [valuePredicate]
     *
     * @param valuePredicate should return true if value is expectation
     */
    fun assertFirstValue(valuePredicate: T.() -> Boolean): LiveDataTestObserver<T> {
            return assertValueAt(0, valuePredicate)
    }

    /**
     * Assert this observer received the next value based on last asserted position. Fail if no asserted position before
     *
     * @param valuePredicate should return true if value is expectation
     */
    fun assertNextValue(valuePredicate: T.() -> Boolean): LiveDataTestObserver<T> {
            if (currentAssertingPosition == null) {
                throw fail("No previous assertion")
            }
            return assertValueAt(currentAssertingPosition!! + 1, valuePredicate)
    }

    /**
     * Assert this observer received the previous value based on last asserted position. Fail if no asserted position before
     *
     * @param valuePredicate should return true if value is expectation
     */
    fun assertPreviousValue(valuePredicate: T.() -> Boolean): LiveDataTestObserver<T> {
            if (currentAssertingPosition == null) {
                throw fail("No previous assertion")
            }
            return assertValueAt(currentAssertingPosition!! - 1, valuePredicate)
    }

    fun assertNoMoreValues() {
            if (currentAssertingPosition == null) {
                throw fail("No previous assertion, use assertNoValue instead")
            }
            if (currentAssertingPosition!! >= values.size) {
                throw fail("Expect no more values but has")
            }
    }

    /**
     * Assert that this Observer haven't received any value yet
     */
    fun assertNoValue(): LiveDataTestObserver<T> {
            if (values.isNotEmpty()) {
                throw fail("Expect no value but has")
            }
            return this
    }

    /**
     * Assert that this Observer received [count] values
     */
    fun assertValuesCount(count: Int): LiveDataTestObserver<T> {
            if (values.size != count) {
                throw fail("Expect $count values but has")
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
                ". " + "Values (size = ${values.size}): \n" + printValues()

            return AssertionError(b)
    }

    private fun printValues(): String {
            val sb = StringBuilder()
            values.forEachIndexed { i, v -> sb.append("$i: $v").append("\n") }
            return sb.toString()
    }

    /**
     * Compares two potentially null objects with each other
     * @param o1 the first object
     * @param o2 the second object
     * @return the comparison result
     */
    private fun equals(o1: Any?, o2: Any?): Boolean {
        return o1 == o2 || (o1 != null && o1 == o2)
    }
}
