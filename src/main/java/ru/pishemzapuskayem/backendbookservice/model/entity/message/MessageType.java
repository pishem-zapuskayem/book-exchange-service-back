package ru.pishemzapuskayem.backendbookservice.model.entity.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {
    OUTGOING(0, "Исходящее"),
    INCOMING(1, "Входящее");

    private final int id;
    private final String name;

    public static MessageType byId(int id) {
        for (MessageType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }
}
