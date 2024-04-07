package ru.pishemzapuskayem.backendbookservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ListType {
    OFFER_LIST(0),
    WISH_LIST(1);

    private final int id;

    public static ListType byId(int id) {
        for (ListType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }
}
