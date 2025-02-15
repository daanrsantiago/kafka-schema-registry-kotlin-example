package dev.danielsantiago.kafka.schema.registry.example

import dev.danielsantiago.kafka.schema.registry.example.consumer.OrderCreatedConsumer

fun main() {
    val orderCreatedConsumer = OrderCreatedConsumer()

    orderCreatedConsumer.listenToOrders { orderCreatedEvent ->
        println(orderCreatedEvent.toString())
    }
}