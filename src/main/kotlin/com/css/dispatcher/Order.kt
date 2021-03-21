package com.css.dispatcher

data class Order(
        val id: String,
        val name: String,
        val prepTime: Int
)

data class OrderDTO(
    val id: String? = null,
    val name: String? = null,
    val prepTime: Int? = null
)