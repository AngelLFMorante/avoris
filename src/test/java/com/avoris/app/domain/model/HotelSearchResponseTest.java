package com.avoris.app.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HotelSearchResponseTest {

    @Test
    void testHotelSearchResponse() {
        String searchId = "1234";
        HotelSearch hotelSearch = new HotelSearch();
        long count = 10;

        HotelSearchResponse response = new HotelSearchResponse(searchId, hotelSearch, count);

        assertEquals(searchId, response.searchId());
        assertEquals(hotelSearch, response.search());
        assertEquals(count, response.count());
    }
}
