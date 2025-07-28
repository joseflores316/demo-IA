package com.jose.demoia.actriz.domain.model;

import javax.persistence.*;

@Entity
@Table(name = "actriz_caracteristica")
public class ActrizCaracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actriz_id", nullable = false)
    private Actriz actriz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caracteristica_id", nullable = false)
    private Caracteristica caracteristica;

    // Constructor vacío requerido por JPA
    public ActrizCaracteristica() {}

    // Constructor con parámetros
    public ActrizCaracteristica(Actriz actriz, Caracteristica caracteristica) {
        this.actriz = actriz;
        this.caracteristica = caracteristica;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Actriz getActriz() {
        return actriz;
    }

    public void setActriz(Actriz actriz) {
        this.actriz = actriz;
    }

    public Caracteristica getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(Caracteristica caracteristica) {
        this.caracteristica = caracteristica;
    }
}
