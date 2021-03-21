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
            stats.calculateStatistics(foodWaitTime * 1000L, courierWaitTime * 1000L)
        }

        timer.schedule(timerTask(action), order.prepTime * 1000L)
    }
}
