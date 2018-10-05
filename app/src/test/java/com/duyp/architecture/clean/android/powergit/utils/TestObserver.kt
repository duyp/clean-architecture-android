package com.duyp.architecture.clean.android.powergit.utils

import android.arch.lifecycle.Observer

/**
 * Used for testing [android.arch.lifecycle.LiveData]
 *
 * @param <T> type of data
 */
@Suppress("unused")
class TestObserver<T> : Observer<T> {

    private val values: MutableList<T> = ArrayList()

    override fun onChanged(t: T?) {
        t?.let { values.add(it) }
    }

    /**
     * Assert that this Observer received the value at given [position] through [onChanged] with provided [valuePredicate]
     *
     * @param valuePredicate should return true if value is expectation
     *
     * @return [OnGoingStub] for continuous assertion
     */
    fun assertValue(position: Int, valuePredicate: T.() -> Boolean): OnGoingStub<T> {
        failIfNoValue()

        if (!valuePredicate.invoke(this.values[position])) {
            throw fail("Value not present")
        }

        return OnGoingStub(this, position)
    }

    /**
     * Assert this observer received the last value with provided [valuePredicate]
     *
     * @param valuePredicate should return true if value is expectation
     *
     * @return [OnGoingStub] for continuous assertion
     */
    fun assertLastValue(valuePredicate: T.() -> Boolean): OnGoingStub<T> {
        return assertValue(values.size - 1, valuePredicate)
    }

    /**
     * Assert this observer received the first value with provided [valuePredicate]
     *
     * @param valuePredicate should return true if value is expectation
     *
     * @return [OnGoingStub] for continuous assertion
     */
    fun assertFirstValue(valuePredicate: T.() -> Boolean): OnGoingStub<T> {
        return assertValue(0, valuePredicate)
    }

    /**
     * Assert that this Observer received the value at given [position] which is equal to the given value
     *
     * @param value value to assert
     */
    fun assertValueEquals(position: Int, value: T): TestObserver<T> {
        failIfNoValue()
        if (!equals(this.values[position], value)) {
            throw fail("Value not equal, expected: $value, actual: ${this.values[position]}")
        }
        return this
    }

    /**
     * Assert that this Observer received the last value which is equal to the given value
     * @param value value to assert
     */
    fun assertLastValueEquals(value: T): TestObserver<T> {
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

    /**
     * Assert that this Observer received [count] values
     */
    fun assertValuesCount(count: Int): TestObserver<T> {
        if (values.size != count) {
            throw fail("Expect $count values but has")
        }
        return this
    }

    /**
     * Assert the number of received values by given [countPredicate]
     *
     * @param countPredicate should return true if the count number is expectation
     */
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
                ". " + "Values (size = ${values.size}): \n" + printValues()

        return AssertionError(b)
    }

    private fun printValues(): String {
        val sb = StringBuilder()
        values.forEachIndexed{ i, v -> sb.append("$i: $v").append("\n") }
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

    /**
     * For continuous [TestObserver] assertion, it offers asserting previous and next values of the [TestObserver]
     */
    class OnGoingStub<T> (
            private val testObserver: TestObserver<T>,
            private val currentPosition: Int
    ) {

        /**
         * Assert the [TestObserver]'s previous received value from the this position, see [TestObserver.assertValue]
         */
        fun withPrevious(valuePredicate: T.() -> Boolean): OnGoingStub<T> {
            return testObserver.assertValue(currentPosition - 1, valuePredicate)
        }

        /**
         * Assert the [TestObserver]'s next received value from this position, see [TestObserver.assertValue]
         */
        fun withNext(valuePredicate: T.() -> Boolean): OnGoingStub<T> {
            return testObserver.assertValue(currentPosition + 1, valuePredicate)
        }

        /**
         * Assert the [TestObserver] has no previous value from this position
         */
        fun noPrevious(): TestObserver<T> {
            return testObserver.assertValuesCount { it == 1 }
        }

        /**
         * Assert the [TestObserver] has no next value of this position
         */
        fun noNext(): TestObserver<T> {
            return testObserver.assertValuesCount { currentPosition == it - 1 }
        }
    }
}
