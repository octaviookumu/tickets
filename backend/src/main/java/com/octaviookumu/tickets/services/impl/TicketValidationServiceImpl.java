package com.octaviookumu.tickets.services.impl;

import com.octaviookumu.tickets.domain.entities.*;
import com.octaviookumu.tickets.exceptions.QrCodeNotFoundException;
import com.octaviookumu.tickets.exceptions.TicketNotFoundException;
import com.octaviookumu.tickets.repositories.QrCodeRepository;
import com.octaviookumu.tickets.repositories.TicketRepository;
import com.octaviookumu.tickets.repositories.TicketValidationRepository;
import com.octaviookumu.tickets.services.TicketValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional // we'll be making multiple database calls in both methods. Covers both methods
public class TicketValidationServiceImpl implements TicketValidationService {

    private final QrCodeRepository qrCodeRepository;
    private final TicketValidationRepository ticketValidationRepository;
    private final TicketRepository ticketRepository; // validate ticket manually

    @Override
    public TicketValidation validateTicketByQrCode(UUID qrCodeId) {
        // there are ways to improve this in terms of security
        // we're taking the UUID baking it into a qrCode
        // and then using it to lookup the qrCode entity in the db
        QrCode qrCode = qrCodeRepository.findByIdAndStatus(qrCodeId, QrCodeStatusEnum.ACTIVE)
                .orElseThrow(() -> new QrCodeNotFoundException(
                        String.format("QR Code with ID %s was not found", qrCodeId)
                ));

        // get the ticket from the QR Code
        Ticket ticket = qrCode.getTicket();

        return validateTicket(ticket);
    }

    private TicketValidation validateTicket(Ticket ticket) {
        // create ticket validation
        TicketValidation ticketValidation = new TicketValidation();
        ticketValidation.setTicket(ticket);
        ticketValidation.setValidationMethod(TicketValidationMethodEnum.QR_SCAN);

        // a ticket can only be validated once
        // any further attempts to validate it will come back as invalid
        // get all existing validations associated with this ticket
        TicketValidationStatusEnum ticketValidationStatus = ticket.getValidations().stream()
                .filter(v -> TicketValidationStatusEnum.VALID.equals(v.getStatus()))
                .findFirst()
                .map(v -> TicketValidationStatusEnum.INVALID) // if validated before, set it to invalid
                .orElse(TicketValidationStatusEnum.VALID); // if ticket not validated, set it to valid

        ticketValidation.setStatus(ticketValidationStatus);

        return ticketValidationRepository.save(ticketValidation);
    }

    @Override
    public TicketValidation validateTicketManually(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(TicketNotFoundException::new);
        return validateTicket(ticket);
    }
}
