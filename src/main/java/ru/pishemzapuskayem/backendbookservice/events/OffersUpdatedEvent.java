package ru.pishemzapuskayem.backendbookservice.events;

import org.springframework.context.ApplicationEvent;

public class OffersUpdatedEvent extends ApplicationEvent {
    public OffersUpdatedEvent(Object source) {
        super(source);
    }
}
