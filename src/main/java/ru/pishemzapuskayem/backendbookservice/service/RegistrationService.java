package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.model.entity.AccountAddress;
import ru.pishemzapuskayem.backendbookservice.model.entity.FileAttachment;
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
    private final FileAttachmentService fileAttachmentService;

    @Transactional
    public void registrationAccount(Account account, AccountAddress accountAddress, MultipartFile avatar) {
        FileAttachment fileAttachment = fileAttachmentService.saveFile(avatar);
        account.setAvatar(fileAttachment);
        Account accountSave = accountRepository.save(account);
        accountAddress.setAccount(accountSave);
        accountAddressRepository.save(accountAddress);
    }
}
