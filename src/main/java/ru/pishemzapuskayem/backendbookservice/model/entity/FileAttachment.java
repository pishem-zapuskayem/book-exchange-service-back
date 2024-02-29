package ru.pishemzapuskayem.backendbookservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "file_attachment")
public class FileAttachment extends AbstractEntity {
    @Column(name = "path")
    private String path;
    @Column(name = "url")
    private String url;
}
