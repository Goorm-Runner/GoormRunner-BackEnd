package goorm_runner.backend.member.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SexConverter implements AttributeConverter<Sex, Character> {

    @Override
    public Character convertToDatabaseColumn(Sex sex) {
        if (sex == null) {
            return null;
        }
        return sex.getCode();
    }

    @Override
    public Sex convertToEntityAttribute(Character code) {
        if (code == null) {
            return null;
        }
        return Sex.fromCode(code);
    }
}
