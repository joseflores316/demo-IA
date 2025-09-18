package com.jose.demoia.actriz.infrastructure.messaging.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Evento que se dispara cuando se actualiza una actriz existente
 */
public class ActrizActualizadaEvent extends ActrizEvent {

    @JsonProperty("fechaNacimiento")
    private LocalDate fechaNacimiento;

    @JsonProperty("calificacion")
    private BigDecimal calificacion;

    @JsonProperty("paisNombre")
    private String paisNombre;

    @JsonProperty("imagenUrl")
    private String imagenUrl;

    @JsonProperty("cambiosRealizados")
    private String cambiosRealizados;

    // Constructor vacío para Jackson
    public ActrizActualizadaEvent() {
        super();
        setEventType("ACTRIZ_ACTUALIZADA");
    }

    public ActrizActualizadaEvent(Long actrizId, String actrizNombre, LocalDate fechaNacimiento,
                                BigDecimal calificacion, String paisNombre, String imagenUrl,
                                String cambiosRealizados) {
        super("ACTRIZ_ACTUALIZADA", actrizId, actrizNombre);
        this.fechaNacimiento = fechaNacimiento;
        this.calificacion = calificacion;
        this.paisNombre = paisNombre;
        this.imagenUrl = imagenUrl;
        this.cambiosRealizados = cambiosRealizados;
    }

    // Getters y Setters
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public BigDecimal getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(BigDecimal calificacion) {
        this.calificacion = calificacion;
    }

    public String getPaisNombre() {
        return paisNombre;
    }

    public void setPaisNombre(String paisNombre) {
        this.paisNombre = paisNombre;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getCambiosRealizados() {
        return cambiosRealizados;
    }

    public void setCambiosRealizados(String cambiosRealizados) {
        this.cambiosRealizados = cambiosRealizados;
    }

    @Override
    public String toString() {
        return "ActrizActualizadaEvent{" +
                "actrizId=" + getActrizId() +
                ", actrizNombre='" + getActrizNombre() + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", calificacion=" + calificacion +
                ", paisNombre='" + paisNombre + '\'' +
                ", imagenUrl='" + imagenUrl + '\'' +
                ", cambiosRealizados='" + cambiosRealizados + '\'' +
                ", timestamp=" + getTimestamp() +
                '}';
    }
}
