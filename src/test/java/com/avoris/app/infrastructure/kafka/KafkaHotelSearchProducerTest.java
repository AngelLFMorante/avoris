package com.avoris.app.infrastructure.kafka;

import com.avoris.app.domain.model.HotelSearchRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaHotelSearchProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafkaHotelSearchProducer kafkaHotelSearchProducer;

    @Value("${hotel.kafka.topic}")
    private String topic;

    @Test
    void testSend() {
        List<Integer> ages = new ArrayList<>();
        ages.add(30);
        ages.add(29);
        ages.add(1);
        ages.add(3);

        LocalDate checkin = LocalDate.of(2023, 12, 29);
        LocalDate checkout = LocalDate.of(2023, 12, 31);

        HotelSearchRequest request = new HotelSearchRequest("hotel123", checkin, checkout, ages);

        kafkaHotelSearchProducer.send(request);

        String expectedMessage = "hotelId=hotel123,checkIn=2023-12-29,checkOut=2023-12-31,ages=[30, 29, 1, 3]";
        verify(kafkaTemplate).send(eq(topic), eq(expectedMessage));
    }
}
