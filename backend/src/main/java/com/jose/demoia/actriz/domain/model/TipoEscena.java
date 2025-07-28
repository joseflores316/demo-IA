package com.jose.demoia.actriz.domain.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tipo_escena")
public class TipoEscena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @OneToMany(mappedBy = "tipoEscena", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Escena> escenas;

    // Constructor vacío requerido por JPA
    public TipoEscena() {}

    // Constructor con parámetros
    public TipoEscena(String nombre) {
        this.nombre = nombre;
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

    public Set<Escena> getEscenas() {
        return escenas;
    }

    public void setEscenas(Set<Escena> escenas) {
        this.escenas = escenas;
    }
}
