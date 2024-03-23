package ru.pishemzapuskayem.backendbookservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class UserList extends AbstractEntity {
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private TypeList listType;
    @ManyToOne
    @JoinColumn(name = "wishListId", referencedColumnName = "id")
    private WishList wishList;
    @ManyToOne
    @JoinColumn(name = "offerListId", referencedColumnName = "id")
    private OfferList offerList;
}
