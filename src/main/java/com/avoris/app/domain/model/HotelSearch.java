package com.avoris.app.domain.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Represents a hotel search record in the database.
 * This entity stores information about a hotel search, including:
 * - Hotel ID
 * - Check-in and Check-out dates
 * - List of guest ages
 * - A unique search ID
 */
@Entity
@Table(name = "hotel_search")
public class HotelSearch {

    /**
     * Unique identifier for the search.
     * It is used to reference the search in other operations.
     */
    @Id
    @Column(name = "search_id", nullable = false)
    private String searchId;

    /**
     * The unique identifier for the hotel.
     */
    @Column(name = "hotel_id", nullable = false)
    private String hotelId;

    /**
     * The check-in date for the search.
     */
    @Column(name = "check_in", nullable = false)
    private Date checkIn;

    /**
     * The check-out date for the search.
     */
    @Column(name = "check_out", nullable = false)
    private Date checkOut;

    /**
     * List of guest ages for the search.
     * This is stored as an array of integers.
     */
    @Column(name = "ages")
    private List<Integer> ages;

    public HotelSearch(String searchId, String hotelId, Date checkIn, Date checkOut, List<Integer> ages) {
        this.searchId = searchId;
        this.hotelId = hotelId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.ages = ages;
    }

    public HotelSearch() {
    }


    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public List<Integer> getAges() {
        return ages;
    }

    public void setAges(List<Integer> ages) {
        this.ages = ages;
    }
}
