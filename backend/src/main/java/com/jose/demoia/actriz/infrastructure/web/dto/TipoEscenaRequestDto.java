package com.jose.demoia.actriz.infrastructure.web.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TipoEscenaRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    public TipoEscenaRequestDto() {}

    public TipoEscenaRequestDto(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
