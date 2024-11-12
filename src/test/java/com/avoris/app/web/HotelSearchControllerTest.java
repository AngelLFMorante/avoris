package com.avoris.app.web;

import com.avoris.app.application.service.HotelSearchService;
import com.avoris.app.domain.model.HotelSearch;
import com.avoris.app.domain.model.HotelSearchResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HotelSearchController.class)
class HotelSearchControllerTest {

    @MockBean
    private HotelSearchService hotelSearchService;

    @InjectMocks
    private HotelSearchController hotelSearchController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSearchHotel() throws Exception {
        String searchId = "ef1f70f9-1bcd-4625-ab2e-42a1785cf3c2";

        when(hotelSearchService.processSearch(org.mockito.ArgumentMatchers.any()))
                .thenReturn(searchId);

        mockMvc.perform(post("/api/search")
                        .contentType("application/json")
                        .content("{ \"hotelId\": \"1234aBc\", " +
                                "\"checkIn\": \"2023-12-29\"," +
                                " \"checkOut\": \"2023-12-31\"," +
                                " \"ages\": [30, 29, 1, 3] }"
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.searchId", is(searchId)));
    }

    @Test
    void testCountSimilarSearches() throws Exception {
        String searchId = "ef1f70f9-1bcd-4625-ab2e-42a1785cf3c2";

        List<Integer> ages = new ArrayList<>();
        ages.add(30);
        ages.add(29);
        ages.add(1);
        ages.add(3);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        String ini = "2023-12-29 00:00:00.000";
        String out = "2023-12-31 00:00:00.000";

        Date checkin = dateFormat.parse(ini);
        Date checkout = dateFormat.parse(out);

        HotelSearch search  = new HotelSearch(searchId, "1234aBc", checkin, checkout,  ages);
        HotelSearchResponse hotelSearchResponse = new HotelSearchResponse(searchId, search, 2);

        when(hotelSearchService.getCount(searchId))
                .thenReturn(Optional.of(hotelSearchResponse));

        mockMvc.perform(get("/api/count")
                        .param("searchId", searchId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.searchId", is(searchId)))
                .andExpect(jsonPath("$.count", is(2)))
                .andExpect(jsonPath("$.search.hotelId", is("1234aBc")));
    }

    @Test
    void testCountSimilarSearchesNotFound() throws Exception {
        String searchId = "ef1f70f9-1bcd-4625-ab2e-42a1785cf3c3";

        when(hotelSearchService.getCount(searchId))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/count")
                        .param("searchId", searchId))
                .andExpect(status().is5xxServerError());
    }
}
