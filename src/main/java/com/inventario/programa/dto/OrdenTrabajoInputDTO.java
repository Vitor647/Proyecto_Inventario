package com.inventario.programa.dto;

import java.util.List;

public record OrdenTrabajoInputDTO(Long clienteId, Long vehiculoId, String descripcionProblema, List<RepuestoInputDTO> repuestos) {

}
