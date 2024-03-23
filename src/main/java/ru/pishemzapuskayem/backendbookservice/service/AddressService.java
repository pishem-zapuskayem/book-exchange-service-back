package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.model.entity.AccountAddress;
import ru.pishemzapuskayem.backendbookservice.repository.AccountAddressRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {
    private final AccountAddressRepository accountAddressRepository;

    @Transactional
    public AccountAddress createAddress(AccountAddress address) {
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            AccountAddress oldAccountAddress = accountAddressRepository
                    .findByAccountIdAndIsDefault(address.getAccount().getId(), true);
            oldAccountAddress.setIsDefault(false);
            accountAddressRepository.save(oldAccountAddress);
        }
        return accountAddressRepository.save(address);
    }

    public List<AccountAddress> getAccountAddress(Long accountId){
        return accountAddressRepository.findByAccountId(accountId);
    }

    @Transactional
    public void deleteAccountAddress(Long accountAddressId){
        accountAddressRepository.deleteById(accountAddressId);
    }
}
