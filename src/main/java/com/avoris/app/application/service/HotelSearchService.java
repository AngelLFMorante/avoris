package com.avoris.app.application.service;

import com.avoris.app.domain.model.HotelSearch;
import com.avoris.app.domain.model.HotelSearchRequest;
import com.avoris.app.domain.model.HotelSearchResponse;
import com.avoris.app.domain.repository.HotelSearchRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service that processes hotel search requests and interacts with the repository.
 * This service handles the creation of new searches and fetching similar searches.
 */
@Service
public class HotelSearchService {

    private final HotelSearchRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Constructor to inject the hotel search repository and Kafka template.
     *
     * @param repository hotel search repository
     * @param kafkaTemplate Kafka template to send messages
     */
    public HotelSearchService(HotelSearchRepository repository, KafkaTemplate<String, String> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Processes a hotel search request, generates a unique search ID, and saves the search.
     *
     * @param request the hotel search request containing the search details
     * @return the generated search ID
     */
    @Transactional
    public String processSearch(HotelSearchRequest request) {
        // Genera un ID único para la búsqueda
        String searchId = UUID.randomUUID().toString();

        // Convierte las fechas LocalDate a Date
        Date checkInDate = convertToDate(request.checkIn());
        Date checkOutDate = convertToDate(request.checkOut());

        // Crear y guardar el objeto HotelSearch en la base de datos
        HotelSearch hotelSearch = new HotelSearch(
                searchId,
                request.hotelId(),
                checkInDate,
                checkOutDate,
                request.ages()
        );

        // Guardar la búsqueda en la base de datos
        repository.save(hotelSearch);

        // Enviar el mensaje a Kafka
        String message = String.format("{\"hotelId\":\"%s\", \"checkIn\":\"%s\", \"checkOut\":\"%s\", \"ages\":%s}",
                request.hotelId(), request.checkIn(), request.checkOut(), request.ages());
        kafkaTemplate.send("hotel_availability_searches", searchId, message);

        // Retornar el ID de la búsqueda
        return searchId;
    }

    /**
     * Retrieves the count of searches similar to a given search ID.
     *
     * @param searchId the ID of the search to count similar searches
     * @return the count of similar searches wrapped in an Optional, or an empty Optional if no search is found
     */
    public Optional<HotelSearchResponse> getCount(String searchId) {
        return repository.findBySearchId(searchId)
                .map(search -> {
                    // Definir el rango de fechas para las búsquedas similares
                    LocalDate checkInStart = search.getCheckIn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusDays(1);
                    LocalDate checkInEnd = search.getCheckIn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);
                    LocalDate checkOutStart = search.getCheckOut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusDays(1);
                    LocalDate checkOutEnd = search.getCheckOut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);

                    // Contar las búsquedas similares
                    long count = repository.countSimilarSearches(
                            search.getHotelId(),
                            checkInStart, checkInEnd,
                            checkOutStart, checkOutEnd
                    );

                    // Crear la respuesta con el conteo
                    return Optional.of(new HotelSearchResponse(searchId, search, count));
                }).orElse(Optional.empty());
    }

    /**
     * Helper method to convert a LocalDate to a java.util.Date object.
     *
     * @param localDate the LocalDate to be converted
     * @return the equivalent java.util.Date object
     */
    private static java.util.Date convertToDate(LocalDate localDate) {
        return java.util.Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Handles the search request by reusing the `processSearch` method.
     *
     * @param request the hotel search request to process
     */
    public void handleSearch(HotelSearchRequest request) {
        processSearch(request); // Reuses the logic to save the search
    }
}
