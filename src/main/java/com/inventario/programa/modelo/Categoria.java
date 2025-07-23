package com.inventario.programa.modelo;

import java.util.ArrayList; // <-- CORRECCIÓN: Importación añadida
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @Lob
    private String descripcion;

    @JsonManagedReference // <-- CORRECCIÓN: Anotación añadida para romper el bucle
    @OneToMany(mappedBy = "categoria")
    private List<Repuestos> repuestos = new ArrayList<>();

    // Constructores, Getters y Setters
    public Categoria() {
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

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

    public List<Repuestos> getRepuestos() {
        return repuestos;
    }

    public void setRepuestos(List<Repuestos> repuestos) {
        this.repuestos = repuestos;
    }
}
