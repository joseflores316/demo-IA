package com.jose.demoia.actriz.infrastructure.web.dto;

import com.jose.demoia.actriz.domain.model.TipoCaracteristica;

public class CaracteristicaDto {

    private Long id;
    private String nombre;
    private String descripcion;
    private TipoCaracteristica tipo;

    // Constructor vacío
    public CaracteristicaDto() {}

    // Constructor con parámetros
    public CaracteristicaDto(String nombre, String descripcion, TipoCaracteristica tipo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoCaracteristica getTipo() {
        return tipo;
    }

    public void setTipo(TipoCaracteristica tipo) {
        this.tipo = tipo;
    }
}
