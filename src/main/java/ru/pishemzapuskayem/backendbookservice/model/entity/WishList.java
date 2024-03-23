package ru.pishemzapuskayem.backendbookservice.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class WishList extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    private Account user;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Status status;
    @ManyToOne
    @JoinColumn(name = "idUserAddress", referencedColumnName = "id", nullable = false)
    private AccountAddress address;
    @OneToMany(cascade = {CascadeType.PERSIST})
    private List<UserList> userLists;
}
