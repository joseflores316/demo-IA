package com.jose.demoia.actriz.infrastructure.messaging.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Evento que se dispara cuando se crea una nueva actriz
 */
public class ActrizCreadaEvent extends ActrizEvent {

    @JsonProperty("fechaNacimiento")
    private LocalDate fechaNacimiento;

    @JsonProperty("calificacion")
    private BigDecimal calificacion;

    @JsonProperty("paisNombre")
    private String paisNombre;

    @JsonProperty("imagenUrl")
    private String imagenUrl;

    // Constructor vacío para Jackson
    public ActrizCreadaEvent() {
        super();
        setEventType("ACTRIZ_CREADA");
    }

    public ActrizCreadaEvent(Long actrizId, String actrizNombre, LocalDate fechaNacimiento,
                            BigDecimal calificacion, String paisNombre, String imagenUrl) {
        super("ACTRIZ_CREADA", actrizId, actrizNombre);
        this.fechaNacimiento = fechaNacimiento;
        this.calificacion = calificacion;
        this.paisNombre = paisNombre;
        this.imagenUrl = imagenUrl;
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

    @Override
    public String toString() {
        return "ActrizCreadaEvent{" +
                "actrizId=" + getActrizId() +
                ", actrizNombre='" + getActrizNombre() + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", calificacion=" + calificacion +
                ", paisNombre='" + paisNombre + '\'' +
                ", imagenUrl='" + imagenUrl + '\'' +
                ", timestamp=" + getTimestamp() +
                '}';
    }
}
