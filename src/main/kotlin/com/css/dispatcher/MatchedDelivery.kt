package com.css.dispatcher

import java.util.*
import kotlin.concurrent.timerTask

// deliveryManager

class MatchedDelivery (
    private val dispatcher: Dispatcher,
    private val timer: Timer,
    private val stats: Stats
): DeliveryStrategy {
    override fun dispatch(order: Order) {
        val courier = dispatcher.requestCourier()

        val waitTime = order.prepTime - courier.arrivalTime
        val courierWaitTime: Int
        val foodWaitTime: Int

        if (waitTime > 0) {
            courierWaitTime = waitTime
            foodWaitTime = 0
        } else {
            courierWaitTime = 0
            foodWaitTime = waitTime * -1
        }

        val action: TimerTask.() -> Unit = {
            stats.calculateStatistics(foodWaitTime, courierWaitTime * 1000L)
        }

        timer.schedule(timerTask(action), order.prepTime * 1000L)
    }
}





//    fun processOrders(orders: List<Order>) {
//        orders.forEach { order ->
////            val timeToAdd = timeHelper.getCurrentTimeInSeconds() + order.prepTime - 3
////            scheduledOrders[timeToAdd] = order
//        }
//    }
//
//    fun processOrder(order: Order) {
//        var waitTimeTillDispatchInMillis: Long = 0
//        if (order.prepTime > 3) {
//            waitTimeTillDispatchInMillis = (order.prepTime - 3L) * 1000
//        }
//
//        val action: TimerTask.() -> Unit = {
//            val requestedCourier = dispatcher.requestCourier()
//            stats.calculateStatistics(requestedCourier)
//        }
//        timer.schedule(timerTask(action), waitTimeTillDispatchInMillis)
//    }