package ru.pishemzapuskayem.backendbookservice.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category extends AbstractEntity {
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Schema(description = "По умолчанию false. Нужен для проверки по родительским вершинам для указания " +
        "возможности множественного выбора из дочерних категорий")
    private Boolean multiselect;

    @Schema(description = "Используется для построения дерева категорий")
    @Transient
    private Set<Category> children;
}
