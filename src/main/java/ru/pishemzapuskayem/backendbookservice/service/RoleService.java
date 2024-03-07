package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.model.entity.Role;
import ru.pishemzapuskayem.backendbookservice.repository.RoleRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository repository;

    @Transactional
    public Role findOrCreateByName(String roleName) {
        Optional<Role> roleOpt = repository.findByName(roleName);
        return roleOpt.orElseGet(() -> createRole(roleName));
    }

    @Transactional
    public Role createRole(String roleName) {
        Role newRole = new Role();
        newRole.setName(roleName);
        return repository.save(newRole);
    }

}
