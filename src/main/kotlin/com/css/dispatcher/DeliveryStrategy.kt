package com.css.dispatcher

interface DeliveryStrategy {
    fun dispatch(order: Order)
}
