package com.avoris.app.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

/**
 * Data model for a hotel search request.
 */
@Schema(description = "Request model for hotel search")
public record HotelSearchRequest(
        @NotNull(message = "hotelId cannot be null")
        @Schema(description = "Unique ID of the hotel", example = "1234aBc")
        String hotelId,

        @NotNull(message = "checkIn cannot be null")
        @Schema(description = "Check-in date", example = "2023-12-29")
        LocalDate checkIn,

        @NotNull(message = "checkOut cannot be null")
        @Schema(description = "Check-out date", example = "2023-12-31")
        LocalDate checkOut,

        @NotNull
        @Size(min = 1, message = "Ages cannot be empty")
        @Schema(description = "List of ages of guests", example = "[30, 29, 1, 3]")
        List<Integer> ages
) {}
