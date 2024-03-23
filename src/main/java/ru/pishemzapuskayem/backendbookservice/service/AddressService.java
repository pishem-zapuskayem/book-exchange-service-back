package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.model.entity.AccountAddress;
import ru.pishemzapuskayem.backendbookservice.repository.AccountAddressRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {
//    private final UserService userService;
    private final AccountAddressRepository accountAddressRepository;

    @Transactional
    public AccountAddress createAddress(AccountAddress address) {
        return accountAddressRepository.save(address);
    }
}
