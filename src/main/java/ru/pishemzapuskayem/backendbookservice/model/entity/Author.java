package ru.pishemzapuskayem.backendbookservice.model.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class Author extends AbstractEntity {
    private String lastname;
    private String firstname;
}
