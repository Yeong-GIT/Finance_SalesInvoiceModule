package gityeong.SalesInvoice.service;

import gityeong.SalesInvoice.entity.SalesInvoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    @Value("${salesinvoice.topic.name}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, SalesInvoice> kafkaTemplate;

    public void sendMessage(SalesInvoice message) {
        kafkaTemplate.send(topicName, message);
    }
}
