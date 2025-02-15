package dev.danielsantiago.kafka.schema.registry.example

import dev.danielsantiago.kafka.schema.registry.example.model.OrderCreatedEvent
import dev.danielsantiago.kafka.schema.registry.example.model.OrderItem
import dev.danielsantiago.kafka.schema.registry.example.producer.OrderCreatedProducer
import java.time.Instant

fun main() {
    val orderCreatedEvent = OrderCreatedEvent.newBuilder()
        .setOrderId(1)
        .setOrderTimestamp(Instant.now())
        .setCostumerId(1)
        .setItems(
            listOf(
                OrderItem.newBuilder()
                    .setProductId(1)
                    .setQuantity(2)
                    .setPriceCents(500)
                    .build()
            )
        )
        .setAmountCents(1000)
        .build()

    val orderCreatedProducer = OrderCreatedProducer()

    while (true) {
        orderCreatedProducer.publishOrderCreatedEvent(orderCreatedEvent)
        Thread.sleep(10000)
    }
}