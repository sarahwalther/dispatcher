package com.css.dispatcher

interface TimeHelper {
    fun getCurrentTimeInMillis(): Long
}

class DefaultTimeHelper: TimeHelper {
    override fun getCurrentTimeInMillis(): Long {
        return System.currentTimeMillis()
    }

}
