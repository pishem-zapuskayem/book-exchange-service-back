package ru.pishemzapuskayem.backendbookservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "confirm_token")
public class ConfirmToken extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name = "id_account")
    private Account user;

    @Column(name = "expire_at")
    private LocalDateTime expireAt;

    private Boolean isConfirmed;
    private String token;
}
