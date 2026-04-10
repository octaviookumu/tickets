package com.octaviookumu.tickets.services.impl;

import com.octaviookumu.tickets.domain.entities.Ticket;
import com.octaviookumu.tickets.domain.entities.TicketStatusEnum;
import com.octaviookumu.tickets.domain.entities.TicketType;
import com.octaviookumu.tickets.domain.entities.User;
import com.octaviookumu.tickets.exceptions.TicketTypeNotFoundException;
import com.octaviookumu.tickets.exceptions.TicketsSoldOutException;
import com.octaviookumu.tickets.exceptions.UserNotFoundException;
import com.octaviookumu.tickets.repositories.TicketRepository;
import com.octaviookumu.tickets.repositories.TicketTypeRepository;
import com.octaviookumu.tickets.repositories.UserRepository;
import com.octaviookumu.tickets.services.QrCodeService;
import com.octaviookumu.tickets.services.TicketTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private final UserRepository userRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;
    private final QrCodeService qrCodeService;

    @Override
    @Transactional
    public Ticket purchaseTicket(UUID userId, UUID ticketTypeId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                String.format("User with ID %s was not found", userId)
        ));

        // should account for concurrency issue where
        // say two users check on the available tickets at same time while purchasing e.g. 99 tickets
        // means if two users get to the code, one will have to wait while the other gets the lock
        TicketType ticketType = ticketTypeRepository.findByIdWithLock(ticketTypeId)
                .orElseThrow(() -> new TicketTypeNotFoundException(
                        String.format("Ticket type with ID %s was not found", ticketTypeId)
                ));


        int purchasedTickets = ticketRepository.countByTicketTypeId(ticketType.getId());
        Integer totalAvailable = ticketType.getTotalAvailable();

        // two users come along. one grabs the lock goes ahead and does the calculation
        // either continues on or tickets sold out exception
        // releases the lock then the second user comes along
        if (purchasedTickets + 1 > totalAvailable) {
            throw new TicketsSoldOutException();
        }

        Ticket ticket = new Ticket();
        ticket.setStatus(TicketStatusEnum.PURCHASED);
        ticket.setTicketType(ticketType); // putting that link in place
        ticket.setPurchaser(user);

        // we'll need a saved ticket in order to generate a QR Code
        Ticket savedTicket = ticketRepository.save(ticket);

        qrCodeService.generateQrCode(savedTicket); // sets the link between Qr Code and ticket

        // to make sure the link is definitely saved (maybe not needed)
        return ticketRepository.save(savedTicket);


    }
}
