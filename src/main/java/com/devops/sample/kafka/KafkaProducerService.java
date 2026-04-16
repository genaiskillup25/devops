package com.devops.sample.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Future;

public class KafkaProducerService {

    public static final String KEY = "KAYDATE12-" + new Date();

    public static void main(String g[]) {

        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());

        try {
            KafkaProducer<String, Integer> producer = new KafkaProducer<>(props);

            ProducerRecord<String, Integer> record =
                    new ProducerRecord<>("OrderTopic", KEY, 13);

            Future<RecordMetadata> metadata = producer.send(record, (m, e) -> {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    System.out.println("Partition: " + m.partition());
                    System.out.println("Offset: " + m.offset());
                }
            });

            metadata.get();   // 🔥 VERY IMPORTANT

            producer.flush();
            producer.close();

        } catch (Throwable e) {
            System.out.println("Wrong value " + e);
        }
    }
}