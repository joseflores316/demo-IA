package com.jose.demoia.actriz.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class EscenaRequestDto {

    private String descripcion;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaGrabacion;

    private Duration duracion;

    @Size(max = 100, message = "La ubicación no puede exceder los 100 caracteres")
    private String ubicacion;

    private String imagenUrl;

    @NotNull(message = "El tipo de escena es obligatorio")
    private Long tipoEscenaId;

    // Lista de actrices asociadas a la escena
    private List<ActrizEscenaDto> actrices;

    // Constructor vacío
    public EscenaRequestDto() {}

    // Constructor con parámetros
    public EscenaRequestDto(String descripcion, LocalDate fechaGrabacion, Duration duracion,
                           String ubicacion, String imagenUrl, Long tipoEscenaId, List<ActrizEscenaDto> actrices) {
        this.descripcion = descripcion;
        this.fechaGrabacion = fechaGrabacion;
        this.duracion = duracion;
        this.ubicacion = ubicacion;
        this.imagenUrl = imagenUrl;
        this.tipoEscenaId = tipoEscenaId;
        this.actrices = actrices;
    }

    // Getters y Setters
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

    public Long getTipoEscenaId() {
        return tipoEscenaId;
    }

    public void setTipoEscenaId(Long tipoEscenaId) {
        this.tipoEscenaId = tipoEscenaId;
    }

    public List<ActrizEscenaDto> getActrices() {
        return actrices;
    }

    public void setActrices(List<ActrizEscenaDto> actrices) {
        this.actrices = actrices;
    }

    // DTO interno para la relación actriz-escena
    public static class ActrizEscenaDto {
        @NotNull(message = "El ID de la actriz es obligatorio")
        private Long actrizId;

        @Size(max = 50, message = "El papel no puede exceder los 50 caracteres")
        private String papel;

        public ActrizEscenaDto() {}

        public ActrizEscenaDto(Long actrizId, String papel) {
            this.actrizId = actrizId;
            this.papel = papel;
        }

        public Long getActrizId() {
            return actrizId;
        }

        public void setActrizId(Long actrizId) {
            this.actrizId = actrizId;
        }

        public String getPapel() {
            return papel;
        }

        public void setPapel(String papel) {
            this.papel = papel;
        }
    }
}
