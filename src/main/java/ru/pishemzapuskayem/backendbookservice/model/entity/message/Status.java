package ru.pishemzapuskayem.backendbookservice.model.entity.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    NEW(0, "Новый"),
    CLOSED(1, "Закрыто"),
    IN_WORK(2, "В работе"),
    AWAITING(3, "Ожидает подтверждения"),
    FREE(4, "Свободен"),
    RESERVED(5, "Участвует в обмене");

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