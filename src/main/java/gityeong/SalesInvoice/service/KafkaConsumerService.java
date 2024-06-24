package gityeong.SalesInvoice.service;

import org.springframework.kafka.annotation.KafkaListener;

public class KafkaConsumerService {
    @KafkaListener(topics = "sales-invoice-topic", groupId = "group_id")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
    }
}
