package com.inventario.programa.dto;

public record MovimientoInventarioInputDTO(
        Long repuestoId,
        Long clienteId,
        Long usuarioId,
        String tipo,
        int cantidad
        ) {

}
