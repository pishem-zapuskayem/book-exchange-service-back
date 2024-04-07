package ru.pishemzapuskayem.backendbookservice.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.pishemzapuskayem.backendbookservice.model.dto.AccountAddressDTO;
import ru.pishemzapuskayem.backendbookservice.model.entity.AccountAddress;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountAddressMapper {

    private final ModelMapper modelMapper;

    public AccountAddress map(AccountAddressDTO addressDTO){
        return modelMapper.map(addressDTO, AccountAddress.class);
    }

    public List<AccountAddress> map(List<AccountAddressDTO> addressDTO){
        List<AccountAddress> accountAddresses = new ArrayList<>();
        for (AccountAddressDTO accountAddressDTO: addressDTO){
            accountAddresses.add(map(accountAddressDTO));
        }

        return accountAddresses;
    }

    public List<AccountAddressDTO> mapEntityListToDTO(List<AccountAddress> addresses){
        List<AccountAddressDTO> dtos = new ArrayList<>();
        for (AccountAddress address: addresses){
            dtos.add(map(address));
        }
        return dtos;
    }

    public AccountAddressDTO map(AccountAddress address) {
        return modelMapper.map(address, AccountAddressDTO.class);
    }
}
