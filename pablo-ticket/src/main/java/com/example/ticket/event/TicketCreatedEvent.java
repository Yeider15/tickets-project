package com.example.ticket.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TicketCreatedEvent extends ApplicationEvent {

    private final Long ticketId;
    private final String title;

    public TicketCreatedEvent(Object source, Long ticketId, String title) {
        super(source);
        this.ticketId = ticketId;
        this.title = title;
    }
}
