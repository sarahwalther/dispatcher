package com.css.dispatcher

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DispatcherApplication

fun main(args: Array<String>) {
	if (args[0] == "MATCHED") {
		val deliveryStrategy = MatchedDelivery()
		DeliveryManager(deliveryStrategy).processOrders()
	}

	else if(args[0] == "FIRST_IN_FIRST_OUT") {
		val deliveryStrategy = FirstInFirstOutDelivery()
		DeliveryManager(deliveryStrategy).processOrders()
	}
}
