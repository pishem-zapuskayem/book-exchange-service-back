package ru.pishemzapuskayem.backendbookservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pishemzapuskayem.backendbookservice.model.entity.Category;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherOfferDTO {
    private List<Category> categories;
    private Boolean isAgreed;
}
