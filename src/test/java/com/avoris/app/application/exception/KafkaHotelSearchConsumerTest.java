package com.avoris.app.application.exception;

import com.avoris.app.application.service.HotelSearchService;
import com.avoris.app.domain.model.HotelSearchRequest;
import java.time.LocalDate;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@EmbeddedKafka(partitions = 1, topics = "hotel_availability_searches")
@ExtendWith(MockitoExtension.class)
class KafkaHotelSearchConsumerTest {

    @Mock
    private HotelSearchService hotelSearchService;

    @InjectMocks
    private KafkaHotelSearchConsumer kafkaHotelSearchConsumer;

    private static final String HOTEL_ID = "1234";
    private static final String CHECK_IN = "2023-12-29";
    private static final String CHECK_OUT = "2023-12-31";

    @Test
    void testConsume() {
        HotelSearchRequest hotelSearchRequest = new HotelSearchRequest(
                HOTEL_ID, LocalDate.parse(CHECK_IN), LocalDate.parse(CHECK_OUT),
                Arrays.asList(30, 29, 1, 3)
        );

        doNothing().when(hotelSearchService).handleSearch(hotelSearchRequest);

        kafkaHotelSearchConsumer.consume(hotelSearchRequest);

        verify(hotelSearchService, times(1)).handleSearch(hotelSearchRequest);
    }

    @Test
    void testConsumeWithError() {
        HotelSearchRequest hotelSearchRequest = new HotelSearchRequest(
                HOTEL_ID, LocalDate.parse(CHECK_IN), LocalDate.parse(CHECK_OUT),
                Arrays.asList(30, 29, 1, 3)
        );

        doThrow(new RuntimeException("Processing error")).when(hotelSearchService).handleSearch(hotelSearchRequest);

        kafkaHotelSearchConsumer.consume(hotelSearchRequest);

        verify(hotelSearchService, times(1)).handleSearch(hotelSearchRequest);
    }
}
