package dev.danielsantiago.kafka.schema.registry.example.consumer

import dev.danielsantiago.kafka.schema.registry.example.model.OrderCreatedEvent
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


class OrderCreatedConsumer() {

    private val props = Properties()
    private val consumer: KafkaConsumer<String, OrderCreatedEvent>
    private val ORDER_TOPIC = "Orders.order_created"

    init {
        props["bootstrap.servers"] = "localhost:9092"
        props["schema.registry.url"] = "http://localhost:8081"
        props["group.id"] = "test"
        props["enable.auto.commit"] = "true"
        props["auto.commit.interval.ms"] = "1000"
        props["key.deserializer"] = "org.apache.kafka.common.serialization.StringDeserializer"
        props["value.deserializer"] = KafkaAvroDeserializer::class.java
        props["specific.avro.reader"] = "true"

        consumer = KafkaConsumer(props)
        consumer.subscribe(listOf(ORDER_TOPIC))

        Runtime.getRuntime().addShutdownHook(Thread { consumer.close() })
    }

    fun listenToOrders(orderConsumerFunction: OrderConsumerFunction ) {
        while (true) {
            val consumerRecords = consumer.poll(Duration.ofMillis(100))
            consumerRecords.forEach {
                orderConsumerFunction.consume(it.value())
                println(LocalDateTime.ofInstant(Instant.ofEpochMilli(it.timestamp()), ZoneId.of("America/Sao_Paulo")))
            }
        }
    }

    fun interface OrderConsumerFunction {
        fun consume(order: OrderCreatedEvent?)
    }

}