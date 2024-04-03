package ru.pishemzapuskayem.backendbookservice.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.pishemzapuskayem.backendbookservice.model.dto.CreateExchangeRequestDTO;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.model.entity.TypeList;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserList;
import ru.pishemzapuskayem.backendbookservice.model.entity.WishList;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookExchangeMapper {
    private final ModelMapper modelMapper;
    private final BookMapper bookMapper;
    private final AccountAddressMapper addressMapper;
    private final CategoryMapper categoryMapper;

    public OfferList mapOfferList(CreateExchangeRequestDTO requestDTO) {
        OfferList offerList = modelMapper.map(requestDTO.getOffer(), OfferList.class);
        offerList.setBookLiterary(bookMapper.map(requestDTO.getOffer().getBook()));

        List<UserList> userLists = new ArrayList<>();
        userLists.add(
            categoryMapper.mapToList(
            requestDTO.getOffer().getOfferCategoriesIds(),
            TypeList.OFFER_LIST
        ));
        offerList.setUserLists(userLists);

        return offerList;
    }

    public WishList mapWishList(CreateExchangeRequestDTO requestDTO) {
        WishList wishList = modelMapper.map(requestDTO.getWish(), WishList.class);
        wishList.setAddress(addressMapper.map(requestDTO.getAddress()));

        List<UserList> userLists = new ArrayList<>();
        userLists.add(
            categoryMapper.mapToList(
                requestDTO.getOffer().getOfferCategoriesIds(),
                TypeList.WISH_LIST
            ));
        wishList.setUserLists(userLists);

        return wishList;
    }
}
