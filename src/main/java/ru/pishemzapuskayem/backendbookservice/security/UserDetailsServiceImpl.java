package ru.pishemzapuskayem.backendbookservice.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.repository.AccountRepository;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository userRepository;

    public UserDetailsServiceImpl(AccountRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Account> accountOpt = userRepository.findByEmail(username);

        if (accountOpt.isEmpty()) {
            throw new UsernameNotFoundException("No find username");
        } else {
            return new UserDetailsImpl(accountOpt.get());
        }
    }
}
