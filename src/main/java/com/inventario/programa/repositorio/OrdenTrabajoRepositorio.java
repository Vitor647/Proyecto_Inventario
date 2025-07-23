package com.inventario.programa.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inventario.programa.modelo.OrdenTrabajo;
import com.inventario.programa.modelo.OrdenTrabajo.EstadoOrden;

public interface OrdenTrabajoRepositorio extends JpaRepository<OrdenTrabajo, Long> {

    // Consulta para cuando NO hay filtros 
    @Query("SELECT o FROM OrdenTrabajo o JOIN FETCH o.cliente JOIN FETCH o.vehiculo")
    List<OrdenTrabajo> findAllWithClienteAndVehiculo();

    // Consulta para filtrar solo por ESTADO
    @Query("SELECT o FROM OrdenTrabajo o JOIN FETCH o.cliente JOIN FETCH o.vehiculo WHERE o.estado = :estado")
    List<OrdenTrabajo> findByEstadoWithDetails(@Param("estado") EstadoOrden estado);

    // Consulta para filtrar solo por NOMBRE DE CLIENTE
    @Query("SELECT o FROM OrdenTrabajo o JOIN FETCH o.cliente JOIN FETCH o.vehiculo WHERE lower(o.cliente.nombre) LIKE lower(concat('%', :cliente, '%'))")
    List<OrdenTrabajo> findByClienteNombreWithDetails(@Param("cliente") String cliente);

    // Consulta para filtrar por ESTADO Y NOMBRE DE CLIENTE
    @Query("SELECT o FROM OrdenTrabajo o JOIN FETCH o.cliente JOIN FETCH o.vehiculo WHERE o.estado = :estado AND lower(o.cliente.nombre) LIKE lower(concat('%', :cliente, '%'))")
    List<OrdenTrabajo> findByEstadoAndClienteNombreWithDetails(@Param("estado") EstadoOrden estado, @Param("cliente") String cliente);
}
