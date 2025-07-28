package com.jose.demoia.actriz.domain.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "actrices")
public class Actriz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(nullable = false, precision = 3, scale = 1)
    private BigDecimal calificacion;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pais_id", nullable = false)
    private Pais pais;

    @OneToMany(mappedBy = "actriz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ActrizCaracteristica> actrizCaracteristicas;

    @OneToMany(mappedBy = "actriz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ActrizEscena> actrizEscenas;

    // Constructor vacío requerido por JPA
    public Actriz() {}

    // Constructor con parámetros
    public Actriz(String nombre, LocalDate fechaNacimiento, BigDecimal calificacion, Pais pais) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.calificacion = calificacion;
        this.pais = pais;
    }

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
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

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Set<ActrizCaracteristica> getActrizCaracteristicas() {
        return actrizCaracteristicas;
    }

    public void setActrizCaracteristicas(Set<ActrizCaracteristica> actrizCaracteristicas) {
        this.actrizCaracteristicas = actrizCaracteristicas;
    }

    public Set<ActrizEscena> getActrizEscenas() {
        return actrizEscenas;
    }

    public void setActrizEscenas(Set<ActrizEscena> actrizEscenas) {
        this.actrizEscenas = actrizEscenas;
    }
}
