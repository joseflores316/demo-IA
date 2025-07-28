package com.jose.demoia.actriz.infrastructure.web.mapper;

import com.jose.demoia.actriz.domain.model.Escena;
import com.jose.demoia.actriz.domain.model.TipoEscena;
import com.jose.demoia.actriz.infrastructure.web.dto.EscenaRequestDto;
import com.jose.demoia.actriz.infrastructure.web.dto.EscenaResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {TipoEscenaMapper.class})
public interface EscenaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actrizEscenas", ignore = true)
    @Mapping(source = "tipoEscenaId", target = "tipoEscena", qualifiedByName = "idToTipoEscena")
    @Mapping(source = "imagenUrl", target = "imagenUrl")
    Escena toEntity(EscenaRequestDto requestDto);

    @Mapping(source = "tipoEscena", target = "tipoEscena")
    @Mapping(source = "imagenUrl", target = "imagenUrl")
    EscenaResponseDto toResponseDto(Escena escena);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actrizEscenas", ignore = true)
    @Mapping(source = "tipoEscenaId", target = "tipoEscena", qualifiedByName = "idToTipoEscena")
    @Mapping(source = "imagenUrl", target = "imagenUrl")
    void updateEntityFromDto(EscenaRequestDto requestDto, @MappingTarget Escena escena);

    @Named("idToTipoEscena")
    default TipoEscena mapTipoEscenaFromId(Long tipoEscenaId) {
        if (tipoEscenaId == null) {
            return null;
        }
        TipoEscena tipoEscena = new TipoEscena();
        tipoEscena.setId(tipoEscenaId);
        return tipoEscena;
    }
}
