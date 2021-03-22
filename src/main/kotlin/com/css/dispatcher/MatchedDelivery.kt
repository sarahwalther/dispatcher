package com.css.dispatcher

import java.util.*
import kotlin.concurrent.timerTask


class MatchedDelivery (
    private val dispatcher: Dispatcher = DefaultDispatcher(),
    private val timer: Timer = Timer(),
    private val stats: Stats = DefaultStats()
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
            println("Dispatching courier for order #${order.id}")
            stats.calculateStatistics(foodWaitTime.toMillis(), courierWaitTime.toMillis())
        }

        timer.schedule(timerTask(action), order.prepTime.toMillis())
    }
}
