package com.duyp.architecture.clean.android.powergit.utils

import com.duyp.architecture.clean.android.powergit.ui.Event

/**
 * Check for the event has expected content
 *
 * @return true if the event not null and the content is the same with expectation
 */
fun <T> Event<T>?.assertContent(expected: String) = this?.peekContent() == expected

/**
 * Check for the event has non-null content
 *
 * @return true if the event not null and the content is not null
 */
fun <T> Event<T>?.assertContentNotNull() = this?.peekContent() != null

/**
 * Check for the event has been handled or not
 *
 * @return true if the event is not null and hasn't been handed
 */
fun <T> Event<T>?.assertNotNull(): Boolean = (this != null)

/**
 * Check for the event has been handled or not
 *
 * @return true if the event is not null and hasn't been handed
 */
fun <T> Event<T>?.assertNotNullAndNotHandledYet() = !(this?.hasBeenHandled ?: false)

/**
 * Check for the event has been handled or not
 *
 * @return true if the event is not null has been handed
 */
fun <T> Event<T>?.assertNotNullAndHandled() = this?.hasBeenHandled ?: false

