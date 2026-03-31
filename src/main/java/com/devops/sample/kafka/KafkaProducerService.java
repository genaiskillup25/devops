package com.devops.sample.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Future;

public class KafkaProducerService {

    public static final String KEY = " KAYDATE " + new Date().toString();

    public static void main(String g[]){

        Properties props = new Properties();
        props.setProperty("bootstrap.servers","localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());

        try {
            KafkaProducer<String, Integer> producer = new KafkaProducer<>(props);
            ProducerRecord record = new ProducerRecord<>("test3", KEY, 13);
            Future<RecordMetadata> recordMetadata= producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata1, Exception e) {
                    System.out.println(" Partions : " +recordMetadata1.partition());
                    System.out.println(" offset : " +recordMetadata1.offset());
                    if(e!=null)
                        e.printStackTrace();
                }
            });


        }
        catch(Exception e){
            System.out.println("Wrong value" +e);
        }

    }
}
