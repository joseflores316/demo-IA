package com.jose.demoia.actriz.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "DTO para crear o actualizar una actriz")
public class ActrizRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    @Schema(description = "Nombre completo de la actriz", example = "Emma Stone", required = true)
    private String nombre;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de nacimiento de la actriz", example = "1988-11-06")
    private LocalDate fechaNacimiento;

    @NotNull(message = "La calificación es obligatoria")
    @DecimalMin(value = "1.0", message = "La calificación debe ser mínimo 1.0")
    @DecimalMax(value = "5.0", message = "La calificación debe ser máximo 5.0")
    @Digits(integer = 2, fraction = 1, message = "La calificación debe tener máximo 2 dígitos enteros y 1 decimal")
    @Schema(description = "Calificación de la actriz del 1.0 al 5.0", example = "4.5", required = true)
    private BigDecimal calificacion;

    @NotNull(message = "El país es obligatorio")
    @Schema(description = "ID del país de origen de la actriz", example = "1", required = true)
    private Long paisId;

    @Schema(description = "URL de la imagen de la actriz", example = "http://localhost:8080/api/files/actrices/12345.jpg")
    private String imagenUrl;

    // Constructor vacío
    public ActrizRequestDto() {}

    // Constructor con parámetros
    public ActrizRequestDto(String nombre, LocalDate fechaNacimiento, BigDecimal calificacion, Long paisId, String imagenUrl) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.calificacion = calificacion;
        this.paisId = paisId;
        this.imagenUrl = imagenUrl;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

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

    public Long getPaisId() {
        return paisId;
    }

    public void setPaisId(Long paisId) {
        this.paisId = paisId;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
