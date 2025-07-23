package com.inventario.programa.modelo;

import java.math.BigDecimal; // <-- CORRECCIÓN: Importación añadida
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "repuestos")
public class Repuestos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "LONGTEXT")
    private String descripcion;

    @JsonBackReference // <-- CORRECCIÓN: Anotación añadida para romper el bucle
    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(optional = false)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @Column(length = 50)
    private String ubicacion;

    @Column(name = "stock", columnDefinition = "int default 0")
    private int stock;

    @Column(name = "stock_minimo", columnDefinition = "int default 5")
    private int stockMinimo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "eliminado_en")
    private LocalDateTime eliminadoEn;

    @Column(name = "created_repuesto", updatable = false)
    private LocalDateTime createdRepuesto;

    @Column(name = "updated_repuesto")
    private LocalDateTime updatedRepuesto;

    /**
     * Constructor por defecto necesario para JPA
     */
    public Repuestos() {
        // Constructor vacío requerido por JPA
    }

    protected void onCreate() {
        createdRepuesto = LocalDateTime.now();
        updatedRepuesto = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedRepuesto = LocalDateTime.now();
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public LocalDateTime getEliminadoEn() {
        return eliminadoEn;
    }

    public void setEliminadoEn(LocalDateTime eliminadoEn) {
        this.eliminadoEn = eliminadoEn;
    }

    public LocalDateTime getCreatedRepuesto() {
        return createdRepuesto;
    }

    public void setCreatedRepuesto(LocalDateTime createdRepuesto) {
        this.createdRepuesto = createdRepuesto;
    }

    public LocalDateTime getUpdatedRepuesto() {
        return updatedRepuesto;
    }

    public void setUpdatedRepuesto(LocalDateTime updatedRepuesto) {
        this.updatedRepuesto = updatedRepuesto;
    }
}
