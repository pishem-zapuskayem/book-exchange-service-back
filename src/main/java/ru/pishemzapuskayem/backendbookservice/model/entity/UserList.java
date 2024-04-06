package ru.pishemzapuskayem.backendbookservice.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class UserList extends AbstractEntity {
    @Enumerated(EnumType.ORDINAL)
    private TypeList listType;
    @ManyToOne
    @JoinColumn(name = "wishListId", referencedColumnName = "id")
    private WishList wishList;
    @ManyToOne
    @JoinColumn(name = "offerListId", referencedColumnName = "id")
    private OfferList offerList;
    @OneToMany(cascade = {CascadeType.PERSIST})
    private List<UserValueCategory> categories;

    public UserList setId(Long id) {
        super.setId(id);
        return this;
    }
}
