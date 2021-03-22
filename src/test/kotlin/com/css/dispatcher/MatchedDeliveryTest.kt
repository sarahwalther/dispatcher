package com.css.dispatcher

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.lang.Thread.sleep
import java.util.*

class MatchedDeliveryTest {
    lateinit var matchedDelivery: MatchedDelivery
    private lateinit var dispatcher: Dispatcher
    private lateinit var timer: Timer
    private lateinit var stats: Stats

    private val order = Order(
        id = "456",
        name = "order 2",
        prepTime = 4
    )
    private val zeroTimeOrder = Order(
        id = "zero-time-order",
        name = "zero time order",
        prepTime = 0
    )

    @BeforeEach
    internal fun setUp() {
        dispatcher = mock(Dispatcher::class.java)
        timer = mock(Timer::class.java)
        stats = mock(Stats::class.java)
        matchedDelivery = MatchedDelivery(dispatcher, timer, stats)
    }

    @Test
    internal fun `dispatch dispatches a delivery driver when receiving an order`() {
        val courier = Courier()
        `when`(dispatcher.requestCourier()).thenReturn(courier)

        matchedDelivery.dispatch(zeroTimeOrder)

        verify(dispatcher).requestCourier()
    }

    @Test
    internal fun `dispatch schedules a delivery`() {
        val courier = Courier(3)
        `when`(dispatcher.requestCourier()).thenReturn(courier)

        matchedDelivery.dispatch(order)

        verify(timer).schedule(any(), eq(4000L))
    }

    @Test
    internal fun `when the courier arrives faster than the food prepTime, the system logs 0 for foodWaitTime and the appropriate time for delivery time`() {
        timer = Timer()
        matchedDelivery = MatchedDelivery(dispatcher, timer, stats)

        val courier = Courier(3)
        `when`(dispatcher.requestCourier()).thenReturn(courier)
        matchedDelivery.dispatch(zeroTimeOrder)

        sleep(5)
        verify(stats).calculateStatistics(3000, 0)
    }
}
