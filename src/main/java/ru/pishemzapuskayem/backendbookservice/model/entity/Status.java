package ru.pishemzapuskayem.backendbookservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    NEW(7, "Новый"),
    CLOSED(7, "Закрыто"),
    IN_WORK(7, "В работе");

    private final int id;
    private final String name;

    public static Status byId(int id) {
        for (Status status : values()) {
            if (status.id == id) {
                return status;
            }
        }
        return null;
    }

    public static Status byName(String enColor) {
        for (Status status : values()) {
            if (status.name().equalsIgnoreCase(enColor)) {
                return status;
            }
        }
        return null;
    }
}