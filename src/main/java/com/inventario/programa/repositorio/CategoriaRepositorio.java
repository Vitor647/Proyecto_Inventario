// En CategoriaRepositorio.java
package com.inventario.programa.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inventario.programa.modelo.Categoria;

public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {

    // Método para verificar si existe una categoría por nombre
    boolean existsByNombre(String nombre);

    // Método para contar repuestos asociados a una categoría
    @Query("SELECT COUNT(r) FROM Repuestos r WHERE r.categoria.id = :categoriaId")
    Long countRepuestosByCategoriaId(@Param("categoriaId") Long categoriaId);

    // Método para buscar una categoría por nombre
    Optional<Categoria> findByNombre(String nombre);

}
