package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.exception.ApiException;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.model.entity.AccountAddress;
import ru.pishemzapuskayem.backendbookservice.repository.AccountAddressRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {
//    private final UserService userService;
    private final AccountAddressRepository accountAddressRepository;

    @Transactional
    public AccountAddress createAddress(AccountAddress address) {
        return accountAddressRepository.save(address);
    }

    @Transactional
    public AccountAddress findOrCreateByIndex(AccountAddress accountAddress) {
        if (StringUtils.isEmpty(accountAddress.getAddrIndex())){
            throw new ApiException("index_is_empty");
        }
        Optional<AccountAddress> address = accountAddressRepository.findByAddrIndex(
            accountAddress.getAddrIndex()
        );
        return address.orElseGet(
            () -> createAddress(accountAddress)
        );
    }

    @Transactional
    public AccountAddress findOrCreate(AccountAddress addressWish, Account user) {
        Optional<AccountAddress> address =
                accountAddressRepository.findByAddrIndexAndAddrCityAndAddrStreetAndAddrHouseAndAddrStructureAndAddrApart(
                addressWish.getAddrIndex(), addressWish.getAddrCity(), addressWish.getAddrStreet(),
                addressWish.getAddrHouse(), addressWish.getAddrStructure(), addressWish.getAddrApart()
        );
        return address.orElseGet(
                () -> {
                    addressWish.setAccount(user);
                    return createAddress(addressWish);
                }
        );
    }
}
