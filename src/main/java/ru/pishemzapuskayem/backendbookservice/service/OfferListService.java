package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;
import ru.pishemzapuskayem.backendbookservice.repository.OfferListRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OfferListService {
    private final OfferListRepository offerListRepository;
    private final BookService bookService;
    private final AuthService authService;

    public List<OfferList> getOfferListAccount() {
        Account account = authService.getAuthenticated();
        List<OfferList> offerList = offerListRepository.findByUser(account);
        return offerList;
    }

    public List<OfferList> getOfferListsByStatuses(Set<Status> statuses) {
        Account account = authService.getAuthenticated();
        return offerListRepository.findByStatusesAndUser(
            statuses.stream().map(Status::getId).collect(Collectors.toSet()), account
        );
    }
}
