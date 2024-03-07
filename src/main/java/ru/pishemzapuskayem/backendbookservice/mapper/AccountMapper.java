package ru.pishemzapuskayem.backendbookservice.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.pishemzapuskayem.backendbookservice.model.dto.AccountDTO;
import ru.pishemzapuskayem.backendbookservice.model.dto.RegistrationRequestDTO;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    private final ModelMapper modelMapper;
    private final AccountAddressMapper accountAddressMapper;

    public Account map(RegistrationRequestDTO registrationRequestDTO) {
        Account account = modelMapper.map(registrationRequestDTO, Account.class);
        account.setAccountAddress(
                List.of(
                        accountAddressMapper.map(registrationRequestDTO.getAddress())
                )
        );
        return account;
    }

    public AccountDTO map(Account account) {
        AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);
        if (account.getAvatar() != null){
            accountDTO.setUrlAvatar(account.getAvatar().getUrl());
        }
        return accountDTO;
    }
}
