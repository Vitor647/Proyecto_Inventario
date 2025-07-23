package com.inventario.programa.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventario.programa.modelo.Repuestos;

@Repository

public interface RepuestoRepositorio extends JpaRepository<Repuestos, Long> {

    // Método para encontrar repuestos que no han sido eliminados
    List<Repuestos> findByEliminadoEnIsNull();

    // Método para encontrar repuestos por nombre
    List<Repuestos> findByNombreContainingIgnoreCase(String nombre);

    // Método para encontrar repuestos por categoría
    List<Repuestos> findByCategoriaNombreContainingIgnoreCase(String categoriaNombre);

    // Método para encontrar repuestos por proveedor
    List<Repuestos> findByProveedorNombreContainingIgnoreCase(String proveedorNombre);
}
