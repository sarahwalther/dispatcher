package com.css.dispatcher

import java.util.*
import kotlin.concurrent.timerTask


class CourierArrivalComparator {

    companion object : Comparator<Courier> {

        override fun compare(a: Courier, b: Courier): Int = when {
            a.arrivalPointInTime != b.arrivalPointInTime -> Math.toIntExact(b.arrivalPointInTime - a.arrivalPointInTime)
            else -> a.arrivalTime.compareTo(b.arrivalTime)
        }
    }
}

class FirstInFirstOutDelivery (
    private val dispatcher: Dispatcher = DefaultDispatcher(),
    private val timer: Timer = Timer(),
    private val stats: Stats = DefaultStats(),
    private val timeHelper: TimeHelper = DefaultTimeHelper()
): DeliveryStrategy {
    private val couriers: PriorityQueue<Courier> = PriorityQueue(CourierArrivalComparator)

    override fun dispatch(order: Order) {

        val newCourier = dispatcher.requestCourier()
        newCourier.arrivalPointInTime = timeHelper.getCurrentTimeInMillis() + (newCourier.arrivalTime.toMillis())
        couriers.add(newCourier)

        val action: TimerTask.() -> Unit = {
            println("Dispatching courier for order #${order.id}")
            val courier = couriers.remove()
            val waitTime: Long = timeHelper.getCurrentTimeInMillis() - courier.arrivalPointInTime
            if (waitTime > 0) {
                stats.calculateStatistics(0, waitTime)
            } else {
                stats.calculateStatistics(waitTime * -1, 0)
            }
        }

        timer.schedule(timerTask(action), order.prepTime.toMillis())
    }
}

fun Int.toMillis(): Long {
    return this * 1000L
}
