package com.jose.demoia.actriz.infrastructure.web.mapper;

import com.jose.demoia.actriz.domain.model.TipoEscena;
import com.jose.demoia.actriz.infrastructure.web.dto.TipoEscenaRequestDto;
import com.jose.demoia.actriz.infrastructure.web.dto.TipoEscenaResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TipoEscenaMapper {

    TipoEscena toEntity(TipoEscenaRequestDto dto);

    TipoEscenaResponseDto toResponseDto(TipoEscena entity);

    void updateEntityFromDto(TipoEscenaRequestDto dto, @MappingTarget TipoEscena entity);
}
