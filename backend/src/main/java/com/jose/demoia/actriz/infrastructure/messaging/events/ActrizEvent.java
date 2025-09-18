package com.jose.demoia.actriz.infrastructure.messaging.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Evento base para todos los eventos relacionados con actrices
 */
public abstract class ActrizEvent {

    @JsonProperty("eventId")
    private String eventId;

    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @JsonProperty("actrizId")
    private Long actrizId;

    @JsonProperty("actrizNombre")
    private String actrizNombre;

    // Constructor vacío para Jackson
    public ActrizEvent() {
        this.timestamp = LocalDateTime.now();
        this.eventId = java.util.UUID.randomUUID().toString();
    }

    public ActrizEvent(String eventType, Long actrizId, String actrizNombre) {
        this();
        this.eventType = eventType;
        this.actrizId = actrizId;
        this.actrizNombre = actrizNombre;
    }

    // Getters y Setters
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getActrizId() {
        return actrizId;
    }

    public void setActrizId(Long actrizId) {
        this.actrizId = actrizId;
    }

    public String getActrizNombre() {
        return actrizNombre;
    }

    public void setActrizNombre(String actrizNombre) {
        this.actrizNombre = actrizNombre;
    }

    @Override
    public String toString() {
        return "ActrizEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", timestamp=" + timestamp +
                ", actrizId=" + actrizId +
                ", actrizNombre='" + actrizNombre + '\'' +
                '}';
    }
}
