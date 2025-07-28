package com.jose.demoia.actriz.infrastructure.web.mapper;

import com.jose.demoia.actriz.domain.model.Actriz;
import com.jose.demoia.actriz.domain.model.Pais;
import com.jose.demoia.actriz.infrastructure.web.dto.ActrizRequestDto;
import com.jose.demoia.actriz.infrastructure.web.dto.ActrizResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PaisMapper.class})
public interface ActrizMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "actrizCaracteristicas", ignore = true)
    @Mapping(target = "actrizEscenas", ignore = true)
    @Mapping(source = "paisId", target = "pais", qualifiedByName = "idToPais")
    @Mapping(source = "imagenUrl", target = "imagenUrl")
    Actriz toEntity(ActrizRequestDto requestDto);

    @Mapping(source = "pais", target = "pais")
    @Mapping(source = "imagenUrl", target = "imagenUrl")
    ActrizResponseDto toResponseDto(Actriz actriz);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "actrizCaracteristicas", ignore = true)
    @Mapping(target = "actrizEscenas", ignore = true)
    @Mapping(source = "paisId", target = "pais", qualifiedByName = "idToPais")
    @Mapping(source = "imagenUrl", target = "imagenUrl")
    void updateEntityFromDto(ActrizRequestDto requestDto, @MappingTarget Actriz actriz);

    @Named("idToPais")
    default Pais mapPaisFromId(Long paisId) {
        if (paisId == null) {
            return null;
        }
        Pais pais = new Pais();
        pais.setId(paisId);
        return pais;
    }
}
