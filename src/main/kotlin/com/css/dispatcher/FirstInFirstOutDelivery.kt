package com.css.dispatcher

import java.lang.System.currentTimeMillis
import java.util.*
import kotlin.concurrent.timerTask


interface TimeHelper {
    fun getCurrentTimeInS(): Long
    fun getCurrentTimeInMillis(): Long
}

class DefaultTimeHelper: TimeHelper {
    override fun getCurrentTimeInS(): Long {
        return currentTimeMillis() / 1000
    }

    override fun getCurrentTimeInMillis(): Long {
        return currentTimeMillis()
    }

}

class CustomerComparator {

    companion object : Comparator<Courier> {

        override fun compare(a: Courier, b: Courier): Int = when {
            a.arrivalPointInTime != b.arrivalPointInTime -> a.arrivalPointInTime.toInt() - b.arrivalPointInTime.toInt()
            else -> a.arrivalPointInTime.compareTo(b.arrivalPointInTime)
        }
    }
}

class FirstInFirstOutDelivery (
    private val dispatcher: Dispatcher,
    private val timer: Timer,
    private val stats: Stats,
    private val timeHelper: TimeHelper
): DeliveryStrategy {
    private val couriers: PriorityQueue<Courier> = PriorityQueue(CustomerComparator)


    override fun dispatch(order: Order) {
        val newCourier = dispatcher.requestCourier()
        newCourier.arrivalPointInTime = timeHelper.getCurrentTimeInMillis() + (newCourier.arrivalTime * 1000)
        couriers.add(newCourier)


        val action: TimerTask.() -> Unit = {
            val courier = couriers.remove()
            val courierWaitTime: Long = timeHelper.getCurrentTimeInMillis() - courier.arrivalPointInTime
            stats.calculateStatistics(0, courierWaitTime)
        }

        timer.schedule(timerTask(action), order.prepTime * 1000L)
    }

}
