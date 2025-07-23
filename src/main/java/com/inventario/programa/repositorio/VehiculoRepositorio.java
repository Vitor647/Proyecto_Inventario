// Repositorio para Vehículo
package com.inventario.programa.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventario.programa.modelo.Vehiculo;

public interface VehiculoRepositorio extends JpaRepository<Vehiculo, Long> {

    List<Vehiculo> findByMarca(String marca);

    List<Vehiculo> findByCliente_Id(Long clienteId);

    List<Vehiculo> findByClienteId(Long clienteId);
}
