package ru.pishemzapuskayem.backendbookservice.model.entity.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    NEW(1, "Новый"),
    CLOSED(2, "Закрыто"),
    IN_WORK(3, "В работе"),
    AWAITING(4, "Ожидает подтверждения");

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