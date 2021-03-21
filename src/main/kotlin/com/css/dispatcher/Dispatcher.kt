package com.css.dispatcher

interface  Dispatcher {
    fun requestCourier(): Courier
}

class DefaultDispatcher: Dispatcher {
//    Uniform distribution:
//    R = random number between 0 and 1
//    min + (max - min) * R

    override fun requestCourier(): Courier = Courier()
}