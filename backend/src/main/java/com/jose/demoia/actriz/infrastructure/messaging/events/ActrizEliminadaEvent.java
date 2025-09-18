package com.jose.demoia.actriz.infrastructure.messaging.events;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Evento que se dispara cuando se elimina una actriz
 */
public class ActrizEliminadaEvent extends ActrizEvent {

    @JsonProperty("motivoEliminacion")
    private String motivoEliminacion;

    @JsonProperty("imagenUrlEliminada")
    private String imagenUrlEliminada;

    // Constructor vacío para Jackson
    public ActrizEliminadaEvent() {
        super();
        setEventType("ACTRIZ_ELIMINADA");
    }

    public ActrizEliminadaEvent(Long actrizId, String actrizNombre, String motivoEliminacion, String imagenUrlEliminada) {
        super("ACTRIZ_ELIMINADA", actrizId, actrizNombre);
        this.motivoEliminacion = motivoEliminacion;
        this.imagenUrlEliminada = imagenUrlEliminada;
    }

    // Getters y Setters
    public String getMotivoEliminacion() {
        return motivoEliminacion;
    }

    public void setMotivoEliminacion(String motivoEliminacion) {
        this.motivoEliminacion = motivoEliminacion;
    }

    public String getImagenUrlEliminada() {
        return imagenUrlEliminada;
    }

    public void setImagenUrlEliminada(String imagenUrlEliminada) {
        this.imagenUrlEliminada = imagenUrlEliminada;
    }

    @Override
    public String toString() {
        return "ActrizEliminadaEvent{" +
                "actrizId=" + getActrizId() +
                ", actrizNombre='" + getActrizNombre() + '\'' +
                ", motivoEliminacion='" + motivoEliminacion + '\'' +
                ", imagenUrlEliminada='" + imagenUrlEliminada + '\'' +
                ", timestamp=" + getTimestamp() +
                '}';
    }
}
