package com.inventario.programa.dto;

import java.time.LocalDateTime;

import com.inventario.programa.modelo.OrdenTrabajo;

public record OrdenTrabajoListDTO(Long id, String descripcionProblema, OrdenTrabajo.EstadoOrden estado, LocalDateTime fechaCreacion, String clienteNombre, String vehiculoInfo) {

    public static OrdenTrabajoListDTO fromEntity(OrdenTrabajo ot) {
        String vehiculoInfo = ot.getVehiculo().getMarca() + " " + ot.getVehiculo().getModelo() + " (" + ot.getVehiculo().getMatricula() + ")";
        return new OrdenTrabajoListDTO(ot.getId(), ot.getDescripcionProblema(), ot.getEstado(), ot.getFechaCreacion(), ot.getCliente().getNombre(), vehiculoInfo);
    }
}
