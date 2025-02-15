package dev.danielsantiago.kafka.schema.registry.example.producer

import dev.danielsantiago.kafka.schema.registry.example.model.OrderCreatedEvent
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.Properties
import io.confluent.kafka.serializers.KafkaAvroSerializer
import io.confluent.kafka.serializers.subject.DefaultReferenceSubjectNameStrategy

class OrderCreatedProducer {

    private val props = Properties()
    val producer: KafkaProducer<String, OrderCreatedEvent>
    private val ORDER_TOPIC = "Orders.order_created"

    init {
        props["bootstrap.servers"] = "localhost:9092"
        props["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        props["value.serializer"] = KafkaAvroSerializer::class.java
        props["schema.registry.url"] = "http://localhost:8081"
        props["value.subject.name.strategy"] = "io.confluent.kafka.serializers.subject.TopicRecordNameStrategy"
        props["auto.register.schemas"] = "false"
        producer = KafkaProducer(props)

        Runtime.getRuntime().addShutdownHook(Thread { producer.close() })
    }

    fun publishOrderCreatedEvent(orderCreatedEvent: OrderCreatedEvent) {
        println("Sending order created event ${orderCreatedEvent.orderId}")
        producer.send(ProducerRecord(ORDER_TOPIC,orderCreatedEvent.orderId.toString(), orderCreatedEvent)) { metadata, ex ->
            println("Sent order created event ${orderCreatedEvent.orderId} to partition ${metadata.partition()}")
        }
    }
}