package com.avoris.app.application.service;

import com.avoris.app.domain.model.HotelSearch;
import com.avoris.app.domain.model.HotelSearchRequest;
import com.avoris.app.domain.model.HotelSearchResponse;
import com.avoris.app.domain.repository.HotelSearchRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelSearchServiceTest {

    @Mock
    private HotelSearchRepository repository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private HotelSearchService hotelSearchService;

    private HotelSearchRequest hotelSearchRequest;

    @BeforeEach
    void setUp() {
        hotelSearchRequest = new HotelSearchRequest(
                "hotel123",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                Arrays.asList(30, 29, 1, 3)
        );
    }

    @Test
    void testProcessSearch() {
        when(repository.save(any(HotelSearch.class))).thenReturn(null);

        String result = hotelSearchService.processSearch(hotelSearchRequest);

        assertThat(result).isNotNull();
        verify(kafkaTemplate, times(1)).send(
                eq("hotel_availability_searches"),
                eq(result),
                anyString()
        );
        verify(repository, times(1)).save(any(HotelSearch.class));
    }

    @Test
    void testGetCount_Success() {
        String searchId = "search-id-123";
        HotelSearch hotelSearch = new HotelSearch(
                searchId,
                "hotel123",
                new Date(),
                new Date(),
                Arrays.asList(30, 29, 1, 3)
        );

        when(repository.findBySearchId(searchId)).thenReturn(Optional.of(hotelSearch));
        when(repository.countSimilarSearches(anyString(), any(), any(), any(), any())).thenReturn(5L);

        Optional<HotelSearchResponse> response = hotelSearchService.getCount(searchId);

        assertThat(response).isPresent();
        assertThat(response.get().count()).isEqualTo(5L);
        assertThat(response.get().searchId()).isEqualTo(searchId);
    }

    @Test
    void testGetCount_NoSearchFound() {
        String searchId = "search-id-123";

        when(repository.findBySearchId(searchId)).thenReturn(Optional.empty());

        Optional<HotelSearchResponse> response = hotelSearchService.getCount(searchId);

        assertThat(response).isEmpty();
    }

    @Test
    void testHandleSearch() {
        HotelSearchService spyService = spy(hotelSearchService);

        spyService.handleSearch(hotelSearchRequest);

        verify(spyService, times(1)).processSearch(hotelSearchRequest);
    }
}
