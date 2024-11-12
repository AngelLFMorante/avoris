package com.avoris.app.infrastructure.kafka;

import com.avoris.app.domain.model.HotelSearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Kafka producer to send hotel search requests.
 */
@Service
public class KafkaHotelSearchProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaHotelSearchProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    /**
     * Constructor to configure the producer with the Kafka topic.
     *
     * @param kafkaTemplate Kafka template to send messages
     * @param topic         Kafka topic to publish messages
     */
    public KafkaHotelSearchProducer(KafkaTemplate<String, String> kafkaTemplate,
                                    @Value("${hotel.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    /**
     * Sends a hotel search request to Kafka.
     *
     * @param request the hotel search request
     */
    public void send(HotelSearchRequest request) {
        String message = "hotelId=" + request.hotelId() + ",checkIn=" + request.checkIn() + ",checkOut=" + request.checkOut() + ",ages=" + request.ages();
        logger.info("Sending message to Kafka: {}", message);
        kafkaTemplate.send(topic, message);
    }
}
