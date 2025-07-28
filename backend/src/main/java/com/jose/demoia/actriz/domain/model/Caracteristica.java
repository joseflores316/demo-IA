package com.jose.demoia.actriz.domain.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "caracteristicas")
public class Caracteristica {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private TipoCaracteristica tipo;
    
    @OneToMany(mappedBy = "caracteristica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ActrizCaracteristica> actrizCaracteristicas;
    
    // Constructor vacío requerido por JPA
    public Caracteristica() {}
    
    // Constructor con parámetros
    public Caracteristica(String nombre, String descripcion, TipoCaracteristica tipo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
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
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public TipoCaracteristica getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoCaracteristica tipo) {
        this.tipo = tipo;
    }
    
    public Set<ActrizCaracteristica> getActrizCaracteristicas() {
        return actrizCaracteristicas;
    }
    
    public void setActrizCaracteristicas(Set<ActrizCaracteristica> actrizCaracteristicas) {
        this.actrizCaracteristicas = actrizCaracteristicas;
    }
}
