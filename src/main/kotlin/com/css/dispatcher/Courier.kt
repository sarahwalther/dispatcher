package com.css.dispatcher

data class Courier(val arrivalTime: Int = (3..15).random()) {
    var arrivalPointInTime: Long = arrivalTime * 1000L
}