package ru.pishemzapuskayem.backendbookservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pishemzapuskayem.backendbookservice.model.entity.Category;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishListDTO {
    private Long id;
    private List<Category> categoryList;
    private AccountAddressDTO address;
    private Status status;
}
