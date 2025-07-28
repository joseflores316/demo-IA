package com.jose.demoia.actriz.infrastructure.web.mapper;

import com.jose.demoia.actriz.domain.model.Pais;
import com.jose.demoia.actriz.infrastructure.web.dto.PaisDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaisMapper {

    Pais toEntity(PaisDto paisDto);

    PaisDto toDto(Pais pais);

    void updateEntityFromDto(PaisDto paisDto, @MappingTarget Pais pais);
}
