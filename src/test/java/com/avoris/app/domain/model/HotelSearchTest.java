package com.avoris.app.domain.model;

import java.util.List;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class HotelSearchTest {

    @Test
    void testHotelSearchConstructor() {
        List<Integer> ages = Arrays.asList(30, 29, 1, 3);

        Date checkIn = java.sql.Date.valueOf(LocalDate.of(2023, 12, 29));
        Date checkOut = java.sql.Date.valueOf(LocalDate.of(2023, 12, 31));

        HotelSearch hotelSearch = new HotelSearch("search-id-123", "hotel-id-123", checkIn, checkOut, ages);

        assertThat(hotelSearch.getSearchId()).isEqualTo("search-id-123");
        assertThat(hotelSearch.getHotelId()).isEqualTo("hotel-id-123");
        assertThat(hotelSearch.getCheckIn()).isEqualTo(checkIn);
        assertThat(hotelSearch.getCheckOut()).isEqualTo(checkOut);
        assertThat(hotelSearch.getAges()).isEqualTo(ages);
    }

    @Test
    void testSettersAndGetters() {
        HotelSearch hotelSearch = new HotelSearch();

        List<Integer> ages = Arrays.asList(30, 29, 1, 3);

        Date checkIn = java.sql.Date.valueOf(LocalDate.of(2023, 12, 29));
        Date checkOut = java.sql.Date.valueOf(LocalDate.of(2023, 12, 31));

        hotelSearch.setSearchId("search-id-456");
        hotelSearch.setHotelId("hotel-id-456");
        hotelSearch.setCheckIn(checkIn);
        hotelSearch.setCheckOut(checkOut);
        hotelSearch.setAges(ages);

        assertThat(hotelSearch.getSearchId()).isEqualTo("search-id-456");
        assertThat(hotelSearch.getHotelId()).isEqualTo("hotel-id-456");
        assertThat(hotelSearch.getCheckIn()).isEqualTo(checkIn);
        assertThat(hotelSearch.getCheckOut()).isEqualTo(checkOut);
        assertThat(hotelSearch.getAges()).isEqualTo(ages);
    }
}
