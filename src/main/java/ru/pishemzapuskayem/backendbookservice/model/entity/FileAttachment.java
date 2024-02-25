package ru.pishemzapuskayem.backendbookservice.model.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileAttachment extends AbstractEntity {
    private String path;
    private String url;
}
