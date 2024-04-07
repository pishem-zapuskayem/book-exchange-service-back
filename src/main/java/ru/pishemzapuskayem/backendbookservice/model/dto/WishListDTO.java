package ru.pishemzapuskayem.backendbookservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pishemzapuskayem.backendbookservice.model.entity.Category;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishListDTO {
    private List<Category> categoryList;
    private AccountDTO accountDTO;
}
