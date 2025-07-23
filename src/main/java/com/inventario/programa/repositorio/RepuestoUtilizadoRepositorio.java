package com.inventario.programa.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inventario.programa.modelo.RepuestoUtilizado;

public interface RepuestoUtilizadoRepositorio extends JpaRepository<RepuestoUtilizado, Long> {

    // Busca todos los repuestos utilizados para una orden de trabajo específica.
    @Query("SELECT ru FROM RepuestoUtilizado ru WHERE ru.orden.id = :ordenId")
    List<RepuestoUtilizado> findByOrdenId(@Param("ordenId") Long ordenId);

    // Busca todas las veces que un tipo de repuesto ha sido utilizado.
    @Query("SELECT ru FROM RepuestoUtilizado ru WHERE ru.repuesto.id = :repuestoId")
    List<RepuestoUtilizado> findByRepuestoId(@Param("repuestoId") Long repuestoId);
}
