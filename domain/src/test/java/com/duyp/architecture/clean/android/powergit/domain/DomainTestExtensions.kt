package com.duyp.architecture.clean.android.powergit.domain

fun <T> T.assert(predicate: T.() -> Boolean) {
    if (!predicate.invoke(this)) {
        throw fail("Value not present", this)
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
fun <T> fail(message: String, value: T): AssertionError {
    val b = "$message, actual: \n" + value.toString()

    return AssertionError(b)
}