package ru.pishemzapuskayem.backendbookservice.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.pishemzapuskayem.backendbookservice.constants.Roles;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "account")
@Getter
@Setter
@Accessors(chain = true)
public class Account extends AbstractEntity{

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "username")
    private String username;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "enabled")
    private Boolean enable;

    @OneToMany(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<AccountAddress> accountAddress;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "avatar_id")
    private FileAttachment avatar;

    @Transient
    private boolean isAdmin;


    public boolean isAdmin() {
        return role != null && role.getName().equals(Roles.ROLE_ADMIN);
    }

    public Account setId(Long id) {
        super.setId(id);
        return this;
    }
}
