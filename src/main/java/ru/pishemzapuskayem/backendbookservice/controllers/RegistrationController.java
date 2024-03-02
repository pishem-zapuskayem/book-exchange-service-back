package ru.pishemzapuskayem.backendbookservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.pishemzapuskayem.backendbookservice.mapper.AccountAddressMapper;
import ru.pishemzapuskayem.backendbookservice.mapper.AccountMapper;
import ru.pishemzapuskayem.backendbookservice.model.dto.RegistrationRequestDTO;
import ru.pishemzapuskayem.backendbookservice.service.RegistrationService;

@RestController
@RequestMapping("/api/v1/sign-up")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final AccountMapper accountMapper;
    private final AccountAddressMapper accountAddressMapper;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> signUp(
            @RequestPart("user") RegistrationRequestDTO dto,
            @RequestPart("avatar") MultipartFile avatar
    ) {

        registrationService.registrationAccount(
                accountMapper.map(dto),
                avatar
        );

        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> enableAccount(@RequestParam String token) {
        registrationService.tryEnableAccount(token);
        return ResponseEntity.ok().build();
    }
}
