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

import java.time.LocalDateTime;

import static ru.pishemzapuskayem.backendbookservice.constants.Roles.ROLE_STUDENT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegistrationService {

    private final AccountRepository accountRepository;
    private final RoleService roleService;
    private final FileAttachmentService fileAttachmentService;

    @Transactional
    public void registrationAccount(Account account, MultipartFile avatar) {
        FileAttachment fileAttachment = fileAttachmentService.saveFile(avatar);
        account.setAvatar(fileAttachment);
        account.setCreatedAt(LocalDateTime.now());
        account.setEnable(false);
        account.setRole(roleService.findOrCreateByName(ROLE_STUDENT));
        accountRepository.save(account);
    }
}
