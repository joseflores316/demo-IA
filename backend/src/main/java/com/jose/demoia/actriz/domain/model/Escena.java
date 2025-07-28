package com.jose.demoia.actriz.domain.model;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "escenas")
public class Escena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_grabacion")
    private LocalDate fechaGrabacion;

    @Column(name = "duracion")
    private Duration duracion;

    @Column(length = 100)
    private String ubicacion;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_escena_id", nullable = false)
    private TipoEscena tipoEscena;

    @OneToMany(mappedBy = "escena", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ActrizEscena> actrizEscenas;

    // Constructor vacío requerido por JPA
    public Escena() {}

    // Constructor con parámetros
    public Escena(String descripcion, LocalDate fechaGrabacion, Duration duracion, String ubicacion, String imagenUrl, TipoEscena tipoEscena) {
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

    public TipoEscena getTipoEscena() {
        return tipoEscena;
    }

    public void setTipoEscena(TipoEscena tipoEscena) {
        this.tipoEscena = tipoEscena;
    }

    public Set<ActrizEscena> getActrizEscenas() {
        return actrizEscenas;
    }

    public void setActrizEscenas(Set<ActrizEscena> actrizEscenas) {
        this.actrizEscenas = actrizEscenas;
    }
}
