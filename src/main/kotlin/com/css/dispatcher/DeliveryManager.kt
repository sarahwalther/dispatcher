package com.css.dispatcher

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File


class DeliveryManager(
    private val deliveryStrategy: DeliveryStrategy
) {
    fun <DeliveryStrategy> processOrders() {
        val mapper = ObjectMapper()

        val jsonString: String = File("./src/main/resources/dispatch_orders.json").readText(Charsets.UTF_8)
        val orderDTOs: List<OrderDTO> = mapper.readValue(jsonString, object : TypeReference<List<OrderDTO?>?>() {})

        val orders = orderDTOs.toOrders()

        orders.forEach {
            deliveryStrategy.dispatch(it)
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