package com.octaviookumu.tickets.services;

import com.octaviookumu.tickets.domain.entities.QrCode;
import com.octaviookumu.tickets.domain.entities.Ticket;

public interface QrCodeService {
    QrCode generateQrCode(Ticket ticket);
}
