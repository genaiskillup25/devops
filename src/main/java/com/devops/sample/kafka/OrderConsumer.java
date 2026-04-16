package com.devops.sample.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
public class OrderConsumer {

    public static void main(String h[]) {

        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer","org.apache.kafka.common.serialization.IntegerDeserializer");
        props.setProperty("group.id","OrderGroup1");
        props.setProperty("auto.offset.reset","earliest");

        KafkaConsumer<String,Integer> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList("OrderTopic"));

        while (true) {   // 🔥 IMPORTANT LOOP
            ConsumerRecords<String,Integer> orders = consumer.poll(Duration.ofMillis(1000));

            for (ConsumerRecord<String,Integer> order : orders) {
                System.out.println("Product Name: " + order.key());
                System.out.println("Quantity: " + order.value());
                System.out.println("----------------------");
            }
        }
    }
}