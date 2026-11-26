package com.univalle.dogapp.utils

/**
 * Wrapper for one-shot events emitted through LiveData (navigate, show a message,
 * open the biometric dialog). Prevents the event from firing again on rotation or when
 * the LiveData is observed again.
 */
class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    /** Returns the content only the first time; returns null afterwards. */
    fun getContentIfNotHandled(): T? =
        if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
}
