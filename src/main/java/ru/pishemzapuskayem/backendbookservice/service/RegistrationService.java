package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.model.entity.AccountAddress;
import ru.pishemzapuskayem.backendbookservice.model.entity.ConfirmToken;
import ru.pishemzapuskayem.backendbookservice.model.entity.FileAttachment;
import ru.pishemzapuskayem.backendbookservice.repository.AccountAddressRepository;
import ru.pishemzapuskayem.backendbookservice.repository.AccountRepository;
import ru.pishemzapuskayem.backendbookservice.repository.RoleRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static ru.pishemzapuskayem.backendbookservice.constants.Roles.ROLE_STUDENT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegistrationService {

    private final AccountRepository accountRepository;
    private final RoleService roleService;
    private final FileAttachmentService fileAttachmentService;
    private final TokenService tokenService;
    private final MailService mailService;

    @Transactional
    public void registrationAccount(Account account, MultipartFile avatar) {
        FileAttachment fileAttachment = fileAttachmentService.saveFile(avatar);
        account.setAvatar(fileAttachment);
        account.setCreatedAt(LocalDateTime.now());
        account.setEnable(false);
        account.setRole(roleService.findOrCreateByName(ROLE_STUDENT));

        Account created = accountRepository.save(account);

        ConfirmToken token = tokenService.createToken(created);
        mailService.sendToken(created.getEmail(), token);
    }

    @Transactional
    public void tryEnableAccount(String token) {
        Optional<ConfirmToken> confirmTokenOpt = tokenService.find(token);
        if (confirmTokenOpt.isEmpty()) {
            throw new IllegalStateException();
        }
        ConfirmToken confirmToken = confirmTokenOpt.get();
        if (LocalDateTime.now().isAfter(confirmToken.getExpireAt())) {
            throw new IllegalStateException();
        }
        Account account = confirmToken.getUser();
        account.setEnable(true);
        accountRepository.save(account);
        tokenService.confirm(confirmToken);
    }
}
