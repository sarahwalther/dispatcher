package com.css.dispatcher

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.lang.Thread.sleep


class DeliveryManager(
    private val deliveryStrategy: DeliveryStrategy
) {
    fun process2OrdersPerSecond(delay: () -> Unit = { sleep(500) }) {
        val mapper = ObjectMapper()

        val jsonString: String = File("./src/main/resources/dispatch_orders.json").readText(Charsets.UTF_8)
        val orderDTOs: List<OrderDTO> = mapper.readValue(jsonString, mapper.typeFactory.constructCollectionType(MutableList::class.java, OrderDTO::class.java))

        val orders = orderDTOs.toOrders()

        orders.forEach {
            deliveryStrategy.dispatch(it)
            delay()
        }
    }
}

fun List<OrderDTO>.toOrders(): List<Order> {
    return this.mapNotNull { it.toOrder() }
}

fun OrderDTO.toOrder(): Order? {
    if (this.id == null ||
        this.name == null ||
        this.prepTime == null
    ) return null
    return Order(
        this.id,
        this.name,
        this.prepTime
    )
}