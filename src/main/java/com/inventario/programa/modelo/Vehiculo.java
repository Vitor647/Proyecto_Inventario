package com.inventario.programa.modelo;

import java.util.ArrayList;
import java.util.List; // Asegúrate de tener este import

import jakarta.persistence.CascadeType; // Y este también
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;
    private String anio;
    private String matricula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(
            mappedBy = "vehiculo", // Esto le dice a Hibernate: "La columna que nos une está definida en el campo 'vehiculo' de la clase OrdenTrabajo. No crees una columna nueva aquí."
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrdenTrabajo> ordenesDeTrabajo = new ArrayList<>();

    // Getters y Setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<OrdenTrabajo> getOrdenesDeTrabajo() {
        return ordenesDeTrabajo;
    }

    public void setOrdenesDeTrabajo(List<OrdenTrabajo> ordenesDeTrabajo) {
        this.ordenesDeTrabajo = ordenesDeTrabajo;
    }
}
