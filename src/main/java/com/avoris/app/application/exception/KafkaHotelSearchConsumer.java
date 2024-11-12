package com.avoris.app.application.exception;

import com.avoris.app.domain.model.HotelSearchRequest;
import com.avoris.app.application.service.HotelSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer that listens to messages from Kafka and processes hotel search requests.
 * It handles hotel search messages received from Kafka and invokes the corresponding business logic.
 */
@Component("kafkaHotelSearchConsumerException")
public class KafkaHotelSearchConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaHotelSearchConsumer.class);
    private final HotelSearchService hotelSearchService;

    /**
     * Constructor to inject the hotel search service.
     *
     * @param hotelSearchService the hotel search service
     */
    public KafkaHotelSearchConsumer(HotelSearchService hotelSearchService) {
        this.hotelSearchService = hotelSearchService;
    }

    /**
     * Kafka listener method to consume hotel search messages from Kafka.
     * It processes the incoming message and invokes the `handleSearch` method of the service.
     *
     * @param request the hotel search request from the Kafka message
     */
    @KafkaListener(topics = "${hotel.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(HotelSearchRequest request) {
        try {
            hotelSearchService.handleSearch(request);
            logger.info("Message processed successfully: {}", request);
        } catch (Exception e) {
            logger.error("Error processing message: {}", request, e);
        }
    }
}
