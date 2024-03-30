package ru.pishemzapuskayem.backendbookservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pishemzapuskayem.backendbookservice.mapper.AccountMapper;
import ru.pishemzapuskayem.backendbookservice.model.dto.AccountDTO;
import ru.pishemzapuskayem.backendbookservice.model.dto.AccountUpdateDTO;
import ru.pishemzapuskayem.backendbookservice.service.AccountService;

@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@RestController
public class AccountController {

    private final AccountMapper accountMapper;
    private final AccountService accountService;

    @PostMapping("/update")
    @Secured("ROLE_USER")
    public ResponseEntity<Void> updateAccount(
        @RequestBody AccountUpdateDTO accountDTO
    ) {
        accountService.update(accountMapper.map(accountDTO));
        return ResponseEntity.ok().build();
    }
}
