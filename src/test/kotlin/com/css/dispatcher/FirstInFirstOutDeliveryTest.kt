package com.css.dispatcher

import com.nhaarman.mockitokotlin2.argumentCaptor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito.*
import java.lang.Thread.sleep
import java.util.*


class FirstInFirstOutDeliveryTest {
    lateinit var firstInFirstOutDelivery: FirstInFirstOutDelivery
    private lateinit var dispatcher: Dispatcher
    private lateinit var timer: Timer
    private lateinit var stats: Stats
    private lateinit var timeHelper: TimeHelper

//    @Captor
//    private lateinit var foodWaitTimeCaptor: ArgumentCaptor<Int>
//    @Captor
//    private lateinit var courierWaitTimeCaptor: ArgumentCaptor<Long>

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
    fun setUp() {
        dispatcher = mock(Dispatcher::class.java)
        timer = mock(Timer::class.java)
        stats = mock(Stats::class.java)
        timeHelper = mock(TimeHelper::class.java)
        firstInFirstOutDelivery = FirstInFirstOutDelivery(dispatcher, timer, stats, timeHelper)

        val courier = Courier()
        `when`(dispatcher.requestCourier()).thenReturn(courier)
    }

    @Test
    fun `dispatch requests a courier when receiving an order`() {
        firstInFirstOutDelivery.dispatch(zeroTimeOrder)

        verify(dispatcher).requestCourier()
    }

    @Test
    fun `dispatch schedules a delivery`() {
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

        val firstTimeOrderIsReceivedTimeStamp: Long = 100000
        val secondTimeOrderIsReceivedTimeStamp: Long = 500000
        val firstOrderFinishedTimeStamp: Long = firstTimeOrderIsReceivedTimeStamp + (zeroTimeOrder.prepTime * 10000)
        val secondOrderFinishedTimeStamp: Long = secondTimeOrderIsReceivedTimeStamp + (order.prepTime * 1000)

        `when`(timeHelper.getCurrentTimeInMillis())
            .thenReturn(firstTimeOrderIsReceivedTimeStamp)
            .thenReturn(firstOrderFinishedTimeStamp)
            .thenReturn(secondTimeOrderIsReceivedTimeStamp)
            .thenReturn(secondOrderFinishedTimeStamp)

        firstInFirstOutDelivery.dispatch(zeroTimeOrder)
        firstInFirstOutDelivery.dispatch(order)

        val foodWaitTimeCaptor = argumentCaptor<Long>()
        val courierWaitTimeCaptor = argumentCaptor<Long>()

        sleep(16000)
        verify(stats, times(2)).calculateStatistics(foodWaitTimeCaptor.capture(), courierWaitTimeCaptor.capture())

        assertEquals(385000, courierWaitTimeCaptor.firstValue)
        assertEquals(391000, courierWaitTimeCaptor.secondValue)

        assertEquals(0, foodWaitTimeCaptor.firstValue)
        assertEquals(0, foodWaitTimeCaptor.secondValue)
    }

    @Test
    internal fun `when the food is ready and there is not a courier yet, it calculates the wait time correctly`() {
        timer = Timer()
        firstInFirstOutDelivery = FirstInFirstOutDelivery(dispatcher, timer, stats, timeHelper)

        val slowerCourier = Courier(15)
        `when`(dispatcher.requestCourier())
            .thenReturn(slowerCourier)

        val firstTimeOrderIsReceivedTimeStamp: Long = 100000
        val firstOrderFinishedTimeStamp: Long = firstTimeOrderIsReceivedTimeStamp + (zeroTimeOrder.prepTime * 1000)

        `when`(timeHelper.getCurrentTimeInMillis())
            .thenReturn(firstTimeOrderIsReceivedTimeStamp)
            .thenReturn(firstOrderFinishedTimeStamp)

        firstInFirstOutDelivery.dispatch(zeroTimeOrder)

        val foodWaitTimeCaptor = argumentCaptor<Long>()
        val courierWaitTimeCaptor = argumentCaptor<Long>()

        sleep(16000)
        verify(stats).calculateStatistics(foodWaitTimeCaptor.capture(), courierWaitTimeCaptor.capture())

        assertEquals(0, courierWaitTimeCaptor.firstValue)
        assertEquals(14000, foodWaitTimeCaptor.firstValue)
    }
}