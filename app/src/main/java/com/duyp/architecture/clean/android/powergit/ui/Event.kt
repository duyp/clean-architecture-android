package com.duyp.architecture.clean.android.powergit.ui

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    private fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content

    fun get(onEventUnhandledContent: T.() -> Unit) {
        getContentIfNotHandled()?.let { onEventUnhandledContent(it) }
    }

    override fun toString(): String {
        return "Event(content=$content, hasBeenHandled=$hasBeenHandled)"
    }

    companion object {
        fun empty() = Event(Unit)
    }
}