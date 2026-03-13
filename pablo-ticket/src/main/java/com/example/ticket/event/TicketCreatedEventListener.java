package com.example.ticket.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class TicketCreatedEventListener {

    @EventListener
    public void handleTicketCreated(TicketCreatedEvent event) {
        log.info("[EVENTO] Ticket creado - ID: {}, Título: '{}'",
                event.getTicketId(), event.getTitle());


        notifyExternalProcess(event);
    }

    private void notifyExternalProcess(TicketCreatedEvent event) {
        log.info("[PROCESO EXTERNO] Notificación enviada para ticket ID: {}", event.getTicketId());
    }
}
