package ru.pishemzapuskayem.backendbookservice.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class Category extends AbstractEntity {
    private String name;
    @ManyToOne
    @JoinColumn
    private Category parent;
    @Schema(description = "У родительских вершин true or false???? где используется")
    private Boolean multiselect;
}
