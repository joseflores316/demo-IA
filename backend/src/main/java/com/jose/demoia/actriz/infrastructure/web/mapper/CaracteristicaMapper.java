package com.jose.demoia.actriz.infrastructure.web.mapper;

import com.jose.demoia.actriz.domain.model.Caracteristica;
import com.jose.demoia.actriz.infrastructure.web.dto.CaracteristicaDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CaracteristicaMapper {

    Caracteristica toEntity(CaracteristicaDto caracteristicaDto);

    CaracteristicaDto toDto(Caracteristica caracteristica);

    void updateEntityFromDto(CaracteristicaDto caracteristicaDto, @MappingTarget Caracteristica caracteristica);
}
