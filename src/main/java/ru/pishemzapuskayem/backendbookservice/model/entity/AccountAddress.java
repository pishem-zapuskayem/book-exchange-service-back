package ru.pishemzapuskayem.backendbookservice.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "account_address")
@Getter
@Setter
public class AccountAddress extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "id_account")
    private Account account;
    @Column(name = "addr_index")
    private String addrIndex;
    @Column(name = "addr_city")
    private String addrCity;
    @Column(name = "addr_street")
    private String addrStreet;
    @Column(name = "addr_house")
    private String addrHouse;
    @Column(name = "addr_structure")
    private String addrStructure;
    @Column(name = "addr_apart")
    private String addrApart;
    @Column(name = "is_default")
    private Boolean isDefault;
}
