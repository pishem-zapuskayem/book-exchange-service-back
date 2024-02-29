package ru.pishemzapuskayem.backendbookservice.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.pishemzapuskayem.backendbookservice.model.dto.RegistrationRequestDTO;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    private final ModelMapper modelMapper;

    public Account map(RegistrationRequestDTO registrationRequestDTO){
        return modelMapper.map(registrationRequestDTO, Account.class);
    }
}
