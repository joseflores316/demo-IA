package com.jose.demoia.actriz.infrastructure.web.dto;

public class PaisDto {

    private Long id;
    private String nombre;
    private String codigoIso;
    private String banderaUrl;

    // Constructor vacío
    public PaisDto() {}

    // Constructor con parámetros
    public PaisDto(String nombre, String codigoIso, String banderaUrl) {
        this.nombre = nombre;
        this.codigoIso = codigoIso;
        this.banderaUrl = banderaUrl;
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

    public String getCodigoIso() {
        return codigoIso;
    }

    public void setCodigoIso(String codigoIso) {
        this.codigoIso = codigoIso;
    }

    public String getBanderaUrl() {
        return banderaUrl;
    }

    public void setBanderaUrl(String banderaUrl) {
        this.banderaUrl = banderaUrl;
    }
}
