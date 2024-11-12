package com.avoris.app.web;

import com.avoris.app.domain.model.HotelSearch;
import com.avoris.app.domain.model.HotelSearchRequest;
import com.avoris.app.application.service.HotelSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller to handle hotel search operations.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Hotel Search API", description = "API for managing hotel searches")
public class HotelSearchController {

    private final HotelSearchService hotelSearchService;

    /**
     * Constructor to inject the hotel search service.
     *
     * @param hotelSearchService hotel search service
     */
    public HotelSearchController(HotelSearchService hotelSearchService) {
        this.hotelSearchService = hotelSearchService;
    }

    /**
     * Endpoint to perform a hotel search and return a search ID.
     *
     * @param request hotel search request
     * @return a map containing the generated search ID
     */
    @PostMapping("/search")
    @Operation(summary = "Performs a hotel search and returns a search ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public Map<String, String> searchHotel(@Valid @RequestBody HotelSearchRequest request) {
        String searchId = hotelSearchService.processSearch(request);
        return Map.of("searchId", searchId);
    }

    /**
     * Endpoint to get the count of similar searches by a given search ID.
     *
     * @param searchId the search ID
     * @return a map containing the search ID and the count of similar searches
     */
    @GetMapping("/count")
    @Operation(summary = "Gets the count of similar searches by search ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Search ID not found")
    })
    public Map<String, Object> countSimilarSearches(
            @Parameter(description = "The search ID", required = true)
            @RequestParam String searchId) {
        return hotelSearchService.getCount(searchId)
                .map(result -> {
                    Map<String, Object> response = new HashMap<>();
                    // Buscar la búsqueda asociada al searchId
                    HotelSearch search = result.search();
                    // Colocar el searchId y los detalles de la búsqueda
                    response.put("searchId", searchId);
                    response.put("search", Map.of(
                            "hotelId", search.getHotelId(),
                            "checkIn", search.getCheckIn(),
                            "checkOut", search.getCheckOut(),
                            "ages", search.getAges()
                    ));
                    response.put("count", result.count());
                    return response;
                })
                .orElseThrow(() -> new RuntimeException("Search ID not found"));
    }
}
