package com.css.dispatcher

interface Stats {
    fun calculateStatistics(courier: Courier): Statistics
    fun calculateStatistics(foodWaitTime: Int, courierWaitTime: Long): Statistics
}

class DefaultStats: Stats {
    var stats = Statistics(
        ordersProcessed = 0,
        averageFoodWaitTime = null,
        averageCourierWaitTime = null
    )

    override fun calculateStatistics(courier: Courier): Statistics {
        val numberOfOrdersProcessed = stats.ordersProcessed + 1
        val previousAverageFoodWaitTime = stats.averageFoodWaitTime?.times(stats.ordersProcessed)
        val averageFoodWaitTime = (previousAverageFoodWaitTime ?: 0).plus(courier.arrivalTime).div(numberOfOrdersProcessed)
        stats = Statistics(
            averageFoodWaitTime = averageFoodWaitTime,
            averageCourierWaitTime = 0,
            ordersProcessed = numberOfOrdersProcessed
        )
        println("Average food wait time: ${stats.averageFoodWaitTime} milliseconds between order ready and pickup")
        println("Average courier wait time ${stats.averageCourierWaitTime} milliseconds")

        return stats
    }

    override fun calculateStatistics(foodWaitTime: Int, courierWaitTime: Long): Statistics {
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
        println("Average courier wait time ${stats.averageCourierWaitTime} milliseconds")

        return stats
    }


//    Uniform distribution:
//    R = random number between 0 and 1
//    min + (max - min) * R
}