package ru.pishemzapuskayem.backendbookservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.pishemzapuskayem.backendbookservice.model.dto.RegistrationRequestDTO;

@RestController
@RequestMapping("/api/v1/sign-up")
public class RegistrationController {

//    private RegistrationService registrationService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> signUp(
            @RequestPart("user") RegistrationRequestDTO dto,
            @RequestPart("avatar") MultipartFile avatar
    ) {

        return ResponseEntity.ok().build();
    }
}
