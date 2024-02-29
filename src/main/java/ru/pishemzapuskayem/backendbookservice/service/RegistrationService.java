package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.model.entity.AccountAddress;
import ru.pishemzapuskayem.backendbookservice.repository.AccountAddressRepository;
import ru.pishemzapuskayem.backendbookservice.repository.AccountRepository;
import ru.pishemzapuskayem.backendbookservice.repository.RoleRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegistrationService {

    private final AccountRepository accountRepository;
    private final AccountAddressRepository accountAddressRepository;
    private final RoleRepository roleRepository;
    public void registrationAccount(Account account, AccountAddress address, MultipartFile avatar) {

    }
}
