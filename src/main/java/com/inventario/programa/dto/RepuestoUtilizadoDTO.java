package com.inventario.programa.dto;

import java.math.BigDecimal;

import com.inventario.programa.modelo.RepuestoUtilizado;

public record RepuestoUtilizadoDTO(Long id, String nombreRepuesto, int cantidad, BigDecimal precioUnitario) {

    public static RepuestoUtilizadoDTO fromEntity(RepuestoUtilizado ru) {
        return new RepuestoUtilizadoDTO(ru.getId(), ru.getRepuesto().getNombre(), ru.getCantidad(), ru.getRepuesto().getPrecio());
    }
}
