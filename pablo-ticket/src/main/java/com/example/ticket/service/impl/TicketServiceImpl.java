package com.example.ticket.service.impl;

import com.example.ticket.dto.TicketRequest;
import com.example.ticket.dto.TicketResponse;
import com.example.ticket.exception.TicketNotFoundException;
import com.example.ticket.model.Ticket;
import com.example.ticket.repository.TicketRepository;
import com.example.ticket.service.TicketService;
import com.example.ticket.service.resilience.ResilienceStrategy;
import com.example.ticket.service.resilience.ResilienceStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final ResilienceStrategyFactory resilienceStrategyFactory;
    private final ResilienceStrategy resilienceStrategy;

    @Override
    @Transactional
    public TicketResponse registerTicket(TicketRequest request) {
        return resilienceStrategy.execute(() -> {
            Ticket ticket = new Ticket();
            ticket.setTitle(request.getTitle());
            ticket.setDescription(request.getDescription());

            Ticket saved = ticketRepository.save(ticket);
            log.info("Ticket registrado con ID: {}", saved.getId());

            TicketResponse response = toResponse(saved);
            response.setMessage("Correo de confirmación enviado exitosamente a su bandeja de entrada");
            return response;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse getTicketStatus(Long id) {
        return resilienceStrategy.execute(() -> {
            Ticket ticket = ticketRepository.findById(id)
                    .orElseThrow(() -> new TicketNotFoundException(id));
            return toResponse(ticket);
        });
    }

    private TicketResponse toResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }
}
