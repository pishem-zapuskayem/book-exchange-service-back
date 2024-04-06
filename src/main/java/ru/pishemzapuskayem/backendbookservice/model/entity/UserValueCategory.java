package ru.pishemzapuskayem.backendbookservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "UserValueCategory")
public class UserValueCategory extends AbstractEntity {
    @ManyToOne
    @JoinColumn
    private UserList userList;
    @ManyToOne
    @JoinColumn
    private Category category;

    public UserValueCategory setId(Long id) {
        super.setId(id);
        return this;
    }
}
