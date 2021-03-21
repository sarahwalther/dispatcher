package com.css.dispatcher

interface Stats {
    fun calculateStatistics(foodWaitTime: Long, courierWaitTime: Long): Statistics
}

class DefaultStats: Stats {
    var stats = Statistics(
        ordersProcessed = 0,
        averageFoodWaitTime = null,
        averageCourierWaitTime = null
    )

    override fun calculateStatistics(foodWaitTime: Long, courierWaitTime: Long): Statistics {
        val numberOfOrdersProcessed = stats.ordersProcessed + 1

        val previousAverageFoodWaitTime = stats.averageFoodWaitTime?.times(stats.ordersProcessed)
        val averageFoodWaitTime = (previousAverageFoodWaitTime ?: 0).plus(foodWaitTime).div(numberOfOrdersProcessed)

        val previousAverageCourierWaitTime = stats.averageCourierWaitTime?.times(stats.ordersProcessed)
        val averageCourierWaitTime = (previousAverageCourierWaitTime ?: 0).plus(courierWaitTime).div(numberOfOrdersProcessed)

        stats = Statistics(
            averageFoodWaitTime = averageFoodWaitTime,
            averageCourierWaitTime = averageCourierWaitTime,
            ordersProcessed = numberOfOrdersProcessed
        )

        println("Average food wait time: ${stats.averageFoodWaitTime} milliseconds between order ready and pickup")
        println("Average courier wait time ${stats.averageCourierWaitTime} milliseconds between arrival and order pickup")

        return stats
    }
}