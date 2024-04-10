package ru.pishemzapuskayem.backendbookservice.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Status status) {
        if (status == null) {
            return null;
        }
        return status.getId();
    }

    @Override
    public Status convertToEntityAttribute(Integer id) {
        if (id == null) {
            return null;
        }
        return Status.byId(id);
    }
}