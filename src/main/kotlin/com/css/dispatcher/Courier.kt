package com.css.dispatcher

data class Courier(val arrivalTime: Int = (3..15).random()) {
    lateinit var order: Order
    var arrivalPointInTime: Long = arrivalTime * 1000L

    fun deliver(order: Order) {
        this.order = order
    }
}