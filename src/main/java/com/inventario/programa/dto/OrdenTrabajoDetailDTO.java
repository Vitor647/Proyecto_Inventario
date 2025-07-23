package com.inventario.programa.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.inventario.programa.modelo.OrdenTrabajo;

public record OrdenTrabajoDetailDTO(Long id, String descripcionProblema, OrdenTrabajo.EstadoOrden estado, LocalDateTime fechaCreacion, LocalDateTime fechaCompletado, ClienteDTO cliente, VehiculoDTO vehiculo, List<RepuestoUtilizadoDTO> repuestos) {

    public static OrdenTrabajoDetailDTO fromEntity(OrdenTrabajo ot) {
        List<RepuestoUtilizadoDTO> repuestosDTO = (ot.getRepuestosUtilizados() != null) ? ot.getRepuestosUtilizados().stream().map(RepuestoUtilizadoDTO::fromEntity).toList() : Collections.emptyList();
        return new OrdenTrabajoDetailDTO(ot.getId(), ot.getDescripcionProblema(), ot.getEstado(), ot.getFechaCreacion(), ot.getFechaCompletado(), ClienteDTO.fromEntity(ot.getCliente()), VehiculoDTO.fromEntity(ot.getVehiculo()), repuestosDTO);
    }
}
