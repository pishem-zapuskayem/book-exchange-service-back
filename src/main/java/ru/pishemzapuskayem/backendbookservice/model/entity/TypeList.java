package ru.pishemzapuskayem.backendbookservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TypeList {
    OFFER_LIST(0),
    WISH_LIST(1);

    private final int id;

    public static TypeList byId(int id) {
        for (TypeList type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }
}
