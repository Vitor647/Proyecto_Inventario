package com.inventario.programa.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.inventario.programa.modelo.MovimientoInventario;
import com.inventario.programa.modelo.MovimientoInventario.TipoMovimiento;

public interface MovimientoRepositorio extends JpaRepository<MovimientoInventario, Long> {

    List<MovimientoInventario> findByTipo(TipoMovimiento tipo);

    List<MovimientoInventario> findByRepuestosId(Long repuestoId);

    List<MovimientoInventario> findByClienteId(Long clienteId);

    // Carga los movimientos y se trae los datos del repuesto, cliente y usuario en la misma consulta.
    @Query("SELECT m FROM MovimientoInventario m JOIN FETCH m.repuestos LEFT JOIN FETCH m.cliente LEFT JOIN FETCH m.usuario")
    List<MovimientoInventario> findAllWithDetails();
}
