package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.pishemzapuskayem.backendbookservice.exception.ApiException;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.model.entity.ConfirmToken;
import ru.pishemzapuskayem.backendbookservice.model.entity.FileAttachment;
import ru.pishemzapuskayem.backendbookservice.repository.AccountRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static ru.pishemzapuskayem.backendbookservice.constants.Roles.ROLE_USER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RegistrationService {

    private final AccountRepository accountRepository;
    private final RoleService roleService;
    private final FileAttachmentService fileAttachmentService;
    private final TokenService tokenService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registrationAccount(Account account, MultipartFile avatar) {
        if (avatar != null) {
            FileAttachment fileAttachment = fileAttachmentService.saveFile(avatar);
            account.setAvatar(fileAttachment);
        }
        account.setCreatedAt(LocalDateTime.now());
        account.setEnable(false);
        account.setRole(roleService.findOrCreateByName(ROLE_USER));

        Optional<Account> byEmail = accountRepository.findByEmail(account.getEmail());
        Optional<Account> byUsername = accountRepository.findByUsername(account.getUsername());

        if (byEmail.isPresent()){
            throw new ApiException("Такой email уже есть");
        }

        if(byUsername.isPresent()){
            throw new ApiException("Такой username уже используется");
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));

        if (account.getAccountAddress().isEmpty()) {
            throw new ApiException("Адрес не указан");
        }
        account.getAccountAddress().get(0).setIsDefault(true);
        Account created = accountRepository.save(account);

        ConfirmToken token = tokenService.createToken(created);
        try {
            mailService.sendToken(created.getEmail(), token);
        } catch (Exception e){
            log.info("Token таково "+ account.getEmail() +" : "+ token.getToken());
        }
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
