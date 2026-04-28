package com.octaviookumu.tickets.repositories;

import com.octaviookumu.tickets.domain.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    int countByTicketTypeId(UUID ticketTypeId); // JPA will work out a query for this

    Page<Ticket> findByPurchaserId(UUID purchaserId, Pageable pageable);
}
