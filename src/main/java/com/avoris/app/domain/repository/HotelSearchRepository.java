package com.avoris.app.domain.repository;

import com.avoris.app.domain.model.HotelSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repository interface to perform hotel search operations.
 * This extends JpaRepository to provide CRUD operations.
 */
public interface HotelSearchRepository extends JpaRepository<HotelSearch, Long> {

    Optional<HotelSearch> findBySearchId(String searchId);

    /**
     * Counts the number of similar hotel searches based on the hotel ID and the check-in and check-out dates.
     *
     * @param hotelId the hotel ID
     * @param checkInStart the start date range for check-in
     * @param checkInEnd the end date range for check-in
     * @param checkOutStart the start date range for check-out
     * @param checkOutEnd the end date range for check-out
     * @return the count of similar searches
     */
    @Query("SELECT COUNT(h) FROM HotelSearch h WHERE h.hotelId = ?1 AND h.checkIn BETWEEN ?2 AND ?3 AND h.checkOut BETWEEN ?4 AND ?5")
    Long countSimilarSearches(
            String hotelId,
            LocalDate checkInStart,
            LocalDate checkInEnd,
            LocalDate checkOutStart,
            LocalDate checkOutEnd);
}


