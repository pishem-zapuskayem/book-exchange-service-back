package ru.pishemzapuskayem.backendbookservice.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.pishemzapuskayem.backendbookservice.service.RoleService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoleInit implements CommandLineRunner {

    private final RoleService service;

    @Override
    public void run(String... args) {
        List<String> roles = List.of(
                "ROLE_ADMIN",
                "ROLE_USER",
                "ROLE_STAFF"
        );

        for (var roleName : roles) {
            service.findOrCreateByName(roleName);
        }

        log.info("Роли добавлены");
    }
}
