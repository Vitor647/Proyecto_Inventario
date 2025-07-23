package com.inventario.programa.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventario.programa.modelo.Proveedor;

public interface ProveedorRepositorio extends JpaRepository<Proveedor, Long> {

    // Método para buscar solo los proveedores que no han sido eliminados lógicamente.
    List<Proveedor> findByEliminadoEnIsNull();

    // Método para comprobar si un proveedor existe por su nombre.
    boolean existsByNombre(String nombre);

    // Método para buscar un proveedor por su nombre.
    Optional<Proveedor> findByNombre(String nombre);
}
