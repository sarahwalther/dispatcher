package com.css.dispatcher

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class DeliveryManagerTest {
    lateinit var deliveryManager: DeliveryManager
    lateinit var deliveryStrategy: DeliveryStrategy

    @BeforeEach
    fun setUp() {
        deliveryStrategy = mock(DeliveryStrategy::class.java)
        deliveryManager = DeliveryManager(deliveryStrategy)
    }

    @Test
    fun `processOrders schedules the orders`() {
        deliveryManager.processOrders<FirstInFirstOutDelivery>()

        val ordersCaptor = argumentCaptor<Order>()

        verify(deliveryStrategy, times(132)).dispatch(ordersCaptor.capture())

        val expectedFirstOrder = Order(
            id = "a8cfcb76-7f24-4420-a5ba-d46dd77bdffd",
            name = "Banana Split",
            prepTime = 4
        )

        assertEquals(expectedFirstOrder, ordersCaptor.firstValue)
    }
}