package ru.pishemzapuskayem.backendbookservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferDTO {
    private Long id;
    private BookDTO book;
    private Long userId;
    private String isbn;
    private LocalDate yearPublishing;
}
