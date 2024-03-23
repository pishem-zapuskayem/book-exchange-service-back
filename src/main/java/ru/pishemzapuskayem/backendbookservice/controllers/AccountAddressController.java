package ru.pishemzapuskayem.backendbookservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pishemzapuskayem.backendbookservice.mapper.AccountAddressMapper;
import ru.pishemzapuskayem.backendbookservice.model.dto.AccountAddressDTO;
import ru.pishemzapuskayem.backendbookservice.service.AddressService;
import ru.pishemzapuskayem.backendbookservice.service.AuthService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AccountAddressController {

    private final AddressService addressService;
    private final AccountAddressMapper accountAddressMapper;
    private final AuthService authService;

    @GetMapping
    public List<AccountAddressDTO> getAccountAddress(){
        return accountAddressMapper.mapDto(
                addressService.getAccountAddress(authService.getAuthenticated().getId())
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAccountAddress(@RequestParam Long accountAddressId){
        addressService.deleteAccountAddress(accountAddressId);
        return ResponseEntity.ok().build();
    }


}
