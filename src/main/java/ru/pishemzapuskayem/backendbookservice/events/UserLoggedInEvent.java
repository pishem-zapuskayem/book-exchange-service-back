package ru.pishemzapuskayem.backendbookservice.events;

import org.springframework.context.ApplicationEvent;

public class UserLoggedInEvent extends ApplicationEvent {
    public UserLoggedInEvent(Object source) {
        super(source);
    }
}
