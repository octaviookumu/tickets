package com.octaviookumu.tickets.repositories;

import com.octaviookumu.tickets.domain.entities.Event;
import com.octaviookumu.tickets.domain.entities.EventStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    Page<Event> findByOrganizerId(UUID organizerId, Pageable pageable);

    Optional<Event> findByIdAndOrganizerId(UUID id, UUID organizerId);

    Page<Event> findByStatus(EventStatusEnum status, Pageable pageable);

    /**
     * searching for the searchTerm in the name and venue fields
     */
    //    1. value query
    //  •status = 'PUBLISHED': Only include events whose status is PUBLISHED.
    //	•COALESCE(name, '') || ' ' || COALESCE(venue, ''): Combines the name and venue columns into one string, using '' if either is NULL.
    //	•to_tsvector('english', ...): Converts the combined string into a tsvector, a special PostgreSQL format optimized for full-text search.
    //	•plainto_tsquery('english', :searchTerm): Converts the search term (like "music concert") into a tsquery suitable for matching text in PostgreSQL.
    //	•@@: This is the PostgreSQL full-text search match operator. It returns true if the tsvector matches the tsquery.

    // 2. countQuery
    // 	•This is required for Spring Data pagination.
    //	•It counts the total number of matching rows so the Page<Event> knows the total number of pages.

    // 3. nativeQuery = true
    //	•Tells Spring Data not to try to parse this as JPQL, but use it as raw SQL.
    //	•Necessary here because JPQL doesn’t support PostgreSQL full-text search operators like @@.
    @Query(value = "SELECT * FROM events WHERE " +
            "status = 'PUBLISHED' AND " +
            "to_tsvector('english', COALESCE(name, '') || ' ' || COALESCE(venue, '')) " +
            "@@ plainto_tsquery('english', :searchTerm)",
            countQuery = "SELECT count(*) FROM events WHERE " +
                    "status = 'PUBLISHED' AND " +
                    "to_tsvector('english', COALESCE(name, '') || ' ' || COALESCE(venue, '')) " +
                    "@@ plainto_tsquery('english', :searchTerm)",
            nativeQuery = true)
    // since JPA won't work the type of query we want
    Page<Event> searchEvent(@Param("searchTerm") String searchTerm, Pageable pageable);
}
