package com.css.dispatcher

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.any
import org.mockito.Mockito.eq
import org.mockito.Mockito.times
import java.lang.Thread.sleep
import java.util.*

internal class FirstInFirstOutDeliveryTest {
    lateinit var firstInFirstOutDelivery: FirstInFirstOutDelivery
    private lateinit var dispatcher: Dispatcher
    private lateinit var timer: Timer
    private lateinit var stats: Stats
    private lateinit var timeHelper: TimeHelper

    private val order = Order(
        id = "456",
        name = "order 2",
        prepTime = 4
    )
    private val zeroTimeOrder = Order(
        id = "zero-time-order",
        name = "zero time order",
        prepTime = 1
    )

    @BeforeEach
    internal fun setUp() {
        dispatcher = mock(Dispatcher::class.java)
        timer = mock(Timer::class.java)
        stats = mock(Stats::class.java)
        timeHelper = mock(TimeHelper::class.java)
        firstInFirstOutDelivery = FirstInFirstOutDelivery(dispatcher, timer, stats, timeHelper)

        val courier = Courier()
        `when`(dispatcher.requestCourier()).thenReturn(courier)
    }

    @Test
    internal fun `dispatch requests a courier when receiving an order`() {
        firstInFirstOutDelivery.dispatch(zeroTimeOrder)

        verify(dispatcher).requestCourier()
    }

    @Test
    internal fun `dispatch schedules a delivery`() {
        val courier = Courier(3)
        `when`(dispatcher.requestCourier()).thenReturn(courier)

        firstInFirstOutDelivery.dispatch(order)

        verify(timer).schedule(any(), eq(4000L))
    }

    @Test
    fun `when there is already a courier waiting when an order is ready, dispatch will send that courier to deliver the order`() {
        timer = Timer()
        firstInFirstOutDelivery = FirstInFirstOutDelivery(dispatcher, timer, stats, timeHelper)

        val slowerCourier = Courier(15)
        val fasterCourier = Courier(3)
        `when`(dispatcher.requestCourier())
            .thenReturn(slowerCourier)
            .thenReturn(fasterCourier)

        `when`(timeHelper.getCurrentTimeInMillis())
            .thenReturn(100000)
            .thenReturn(300000)
            .thenReturn(500000)
            .thenReturn(700000)

        firstInFirstOutDelivery.dispatch(zeroTimeOrder)
        firstInFirstOutDelivery.dispatch(order)

        sleep(3000)
        verify(stats, times(1)).calculateStatistics(0, 197000L)
        verify(stats, times(1)).calculateStatistics(11, 0)

//        Mockito.verify(timer).schedule(Mockito.any(), Mockito.eq(4000L))
    }
}