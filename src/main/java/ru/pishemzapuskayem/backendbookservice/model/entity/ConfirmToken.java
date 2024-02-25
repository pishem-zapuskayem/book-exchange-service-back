package ru.pishemzapuskayem.backendbookservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmToken extends AbstractEntity{

    private User user;

    private LocalDateTime expireAt;

    private String token;
}
