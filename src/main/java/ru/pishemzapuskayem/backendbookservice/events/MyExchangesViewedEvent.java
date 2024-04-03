package ru.pishemzapuskayem.backendbookservice.events;

import org.springframework.context.ApplicationEvent;

//todo эндпоинт мои обмены ещё не сделан
public class MyExchangesViewedEvent extends ApplicationEvent {
    public MyExchangesViewedEvent(Object source) {
        super(source);
    }
}
