package ru.pishemzapuskayem.backendbookservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractEntity {

    private String lastName;

    private String firstName;

    private String secondName;

    private String email;

    private String password;

    private String username;

    private Integer rating;

    private LocalDateTime createdAt;

    private Boolean enable;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private FileAttachment avatar;
}
