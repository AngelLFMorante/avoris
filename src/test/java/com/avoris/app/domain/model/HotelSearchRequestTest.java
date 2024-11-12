package com.avoris.app.domain.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HotelSearchRequestValidationTest {

    @Test
    void testHotelSearchRequest() {
        String hotelId = "1234aBc";
        LocalDate checkIn = LocalDate.of(2023, 12, 29);
        LocalDate checkOut = LocalDate.of(2023, 12, 31);
        List<Integer> ages = Arrays.asList(30, 29, 1, 3);

        HotelSearchRequest request = new HotelSearchRequest(hotelId, checkIn, checkOut, ages);

        assertEquals(hotelId, request.hotelId());
        assertEquals(checkIn, request.checkIn());
        assertEquals(checkOut, request.checkOut());
        assertEquals(ages, request.ages());
    }
}
