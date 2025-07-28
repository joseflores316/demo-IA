package com.jose.demoia.actriz.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Duration;
import java.time.LocalDate;

public class EscenaResponseDto {

    private Long id;
    private String descripcion;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaGrabacion;

    private Duration duracion;
    private String ubicacion;
    private String imagenUrl;
    private TipoEscenaDto tipoEscena;

    // Constructor vacío
    public EscenaResponseDto() {}

    // Constructor con parámetros
    public EscenaResponseDto(Long id, String descripcion, LocalDate fechaGrabacion,
                           Duration duracion, String ubicacion, String imagenUrl, TipoEscenaDto tipoEscena) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaGrabacion = fechaGrabacion;
        this.duracion = duracion;
        this.ubicacion = ubicacion;
        this.imagenUrl = imagenUrl;
        this.tipoEscena = tipoEscena;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaGrabacion() {
        return fechaGrabacion;
    }

    public void setFechaGrabacion(LocalDate fechaGrabacion) {
        this.fechaGrabacion = fechaGrabacion;
    }

    public Duration getDuracion() {
        return duracion;
    }

    public void setDuracion(Duration duracion) {
        this.duracion = duracion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public TipoEscenaDto getTipoEscena() {
        return tipoEscena;
    }

    public void setTipoEscena(TipoEscenaDto tipoEscena) {
        this.tipoEscena = tipoEscena;
    }
}
