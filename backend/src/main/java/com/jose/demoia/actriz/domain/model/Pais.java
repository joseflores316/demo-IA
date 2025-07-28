package com.jose.demoia.actriz.domain.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "pais")
public class Pais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "codigo_iso", unique = true)
    private String codigoIso;

    @Column(name = "bandera_url", length = 255)
    private String banderaUrl;

    @OneToMany(mappedBy = "pais", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Actriz> actrices;

    // Constructor vacío requerido por JPA
    public Pais() {}

    // Constructor con parámetros
    public Pais(String nombre, String codigoIso, String banderaUrl) {
        this.nombre = nombre;
        this.codigoIso = codigoIso;
        this.banderaUrl = banderaUrl;
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

    public String getCodigoIso() {
        return codigoIso;
    }

    public void setCodigoIso(String codigoIso) {
        this.codigoIso = codigoIso;
    }

    public String getBanderaUrl() {
        return banderaUrl;
    }

    public void setBanderaUrl(String banderaUrl) {
        this.banderaUrl = banderaUrl;
    }

    public Set<Actriz> getActrices() {
        return actrices;
    }

    public void setActrices(Set<Actriz> actrices) {
        this.actrices = actrices;
    }
}
