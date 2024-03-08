package ru.pishemzapuskayem.backendbookservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDTO {
    @Schema(maxLength = 50)
    private String note;

    @Schema(maxLength = 500)
    private String response;

    private BookDTO bookLiterary;

    private AccountDTO account;
}
