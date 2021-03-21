package com.css.dispatcher

data class Statistics(
    var ordersProcessed: Int,
    var averageFoodWaitTime: Long?,
    var averageCourierWaitTime: Long?
)