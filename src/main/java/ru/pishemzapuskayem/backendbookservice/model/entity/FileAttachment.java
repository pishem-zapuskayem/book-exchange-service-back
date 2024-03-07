package ru.pishemzapuskayem.backendbookservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "file")
@NoArgsConstructor
public class FileAttachment extends AbstractEntity {
    @Column(name = "path")
    private String path;
    @Column(name = "url")
    private String url;

    public FileAttachment(String path, String url){
        this.path = path;
        this.url = url;
    }
}
