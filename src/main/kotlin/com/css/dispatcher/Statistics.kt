package com.css.dispatcher

data class Statistics(
    var ordersProcessed: Int,
    var averageFoodWaitTime: Int?,
    var averageCourierWaitTime: Long?
)