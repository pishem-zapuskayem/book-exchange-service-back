package ru.pishemzapuskayem.backendbookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.AccountAddress;

import java.util.List;

@Repository
public interface AccountAddressRepository extends JpaRepository<AccountAddress, Long> {
    AccountAddress findByAccountIdAndIsDefault(Long id, Boolean aBoolean);
    List<AccountAddress> findByAccountId(Long id);
}
