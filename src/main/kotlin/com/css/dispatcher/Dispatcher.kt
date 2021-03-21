package com.css.dispatcher

interface  Dispatcher {
    fun requestCourier(): Courier
}

class DefaultDispatcher: Dispatcher {
    override fun requestCourier(): Courier = Courier()
}