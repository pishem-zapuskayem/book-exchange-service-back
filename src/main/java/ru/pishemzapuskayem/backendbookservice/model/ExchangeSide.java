package ru.pishemzapuskayem.backendbookservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExchangeSide {
    FIRST(0),
    SECOND(1),
    NONE(2);

    private final int id;

    public static ExchangeSide byId(int id) {
        for (ExchangeSide side : values()) {
            if (side.id == id) {
                return side;
            }
        }
        return null;
    }
}
