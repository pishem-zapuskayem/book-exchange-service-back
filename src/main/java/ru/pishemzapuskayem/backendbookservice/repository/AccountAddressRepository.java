package ru.pishemzapuskayem.backendbookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.AccountAddress;

import java.util.Optional;

@Repository
public interface AccountAddressRepository extends JpaRepository<AccountAddress, Long> {
    Optional<AccountAddress> findByAddrIndex(String addrIndex);

    Optional<AccountAddress> findByAddrIndexAndAddrCityAndAddrStreetAndAddrHouseAndAddrStructureAndAddrApart(
            String addrIndex,
            String addrCity,
            String addrStreet,
            String addrHouse,
            String addrStructure,
            String addrApart
    );
}
