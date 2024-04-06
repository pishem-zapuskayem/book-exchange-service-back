package ru.pishemzapuskayem.backendbookservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.pishemzapuskayem.backendbookservice.events.UserLoggedInEvent;
import ru.pishemzapuskayem.backendbookservice.exception.ApiException;
import ru.pishemzapuskayem.backendbookservice.mapper.AccountMapper;
import ru.pishemzapuskayem.backendbookservice.model.dto.AccountDTO;
import ru.pishemzapuskayem.backendbookservice.model.dto.AuthRequestDTO;
import ru.pishemzapuskayem.backendbookservice.model.dto.AuthResponseDTO;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.security.UserDetailsImpl;
import ru.pishemzapuskayem.backendbookservice.security.jwt.JwtUtil;
import ru.pishemzapuskayem.backendbookservice.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AccountMapper accountMapper;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${jwt.tokenExpiresIn}")
    private int tokenExpiresIn;

    @GetMapping("/me")
    public ResponseEntity<AccountDTO> getMe(){
        return ResponseEntity.ok(
                accountMapper.map(
                        authService.getAuthenticated()
                )
        );
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authRequestDTO.getEmail(),
                authRequestDTO.getPassword()
        );

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new ApiException("Неправильные логин или пароль");
        }

        Account account = ((UserDetailsImpl)authentication.getPrincipal()).getUser();

        if (Boolean.FALSE.equals(account.getEnable())){
            throw new ApiException("Ваш аккаунт неактивирован");
        }

        String token = jwtUtil.generateToken(
                account.getEmail(),
                account.getRole().getName(),
                tokenExpiresIn
        );

        eventPublisher.publishEvent(new UserLoggedInEvent(this));

        return ResponseEntity.ok(new AuthResponseDTO(
                token,
                tokenExpiresIn
        ));
    }
}
