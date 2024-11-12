package com.avoris.app.infrastructure.kafka;

import com.avoris.app.application.service.HotelSearchService;
import com.avoris.app.domain.model.HotelSearchRequest;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka consumer to process hotel search requests.
 */
@Service
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
     * Listens to Kafka messages and processes hotel search requests.
     *
     * @param message hotel search request
     */
    @KafkaListener(topics = "${hotel.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        try {

            String[] parts = message.split(",");
            String hotelId = parts[0].split("=")[1];
            String checkInStr = parts[1].split("=")[1];
            String checkOutStr = parts[2].split("=")[1];
            String agesStr = parts[3].split("=")[1];

            List<Integer> ages = Arrays.stream(agesStr.replaceAll("[\\[\\] ]", "").split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            HotelSearchRequest request = new HotelSearchRequest(
                    hotelId,
                    LocalDate.parse(checkInStr),
                    LocalDate.parse(checkOutStr),
                    ages
            );

            hotelSearchService.handleSearch(request);
            logger.info("Message processed successfully: {}", message);
        } catch (Exception e) {
            logger.error("Error processing message: {}", message, e);
        }
    }
}
