package com.jose.demoia.actriz.infrastructure.web.dto;

public class TipoEscenaDto {

    private Long id;
    private String nombre;

    // Constructor vacío
    public TipoEscenaDto() {}

    // Constructor con parámetros
    public TipoEscenaDto(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
