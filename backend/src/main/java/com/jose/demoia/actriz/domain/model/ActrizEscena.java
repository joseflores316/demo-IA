package com.jose.demoia.actriz.domain.model;

import javax.persistence.*;

@Entity
@Table(name = "actriz_escena")
public class ActrizEscena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actriz_id", nullable = false)
    private Actriz actriz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escena_id", nullable = false)
    private Escena escena;

    @Column(length = 50)
    private String papel;

    // Constructor vacío requerido por JPA
    public ActrizEscena() {}

    // Constructor con parámetros
    public ActrizEscena(Actriz actriz, Escena escena, String papel) {
        this.actriz = actriz;
        this.escena = escena;
        this.papel = papel;
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

    public Escena getEscena() {
        return escena;
    }

    public void setEscena(Escena escena) {
        this.escena = escena;
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }
}
