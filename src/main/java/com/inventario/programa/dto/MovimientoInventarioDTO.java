package com.inventario.programa.dto;

import java.time.LocalDateTime;

import com.inventario.programa.modelo.MovimientoInventario;

public record MovimientoInventarioDTO(
        Long id,
        String tipo,
        int cantidad,
        LocalDateTime fecha,
        String repuestoNombre,
        String clienteNombre,
        String usuarioNombre
        ) {

    public static MovimientoInventarioDTO fromEntity(MovimientoInventario m) {
        String cNombre = (m.getCliente() != null) ? m.getCliente().getNombre() : "N/A";
        // CORRECCIÓN: Ahora el método getUsuario() existe
        String uNombre = (m.getUsuario() != null) ? m.getUsuario().getNombre() : "Sistema";

        return new MovimientoInventarioDTO(
                m.getId(),
                m.getTipo().toString(),
                m.getCantidad(),
                m.getFechaMovimiento(),
                // CORRECCIÓN: Usamos getRepuestos() (plural)
                m.getRepuestos().getNombre(),
                cNombre,
                uNombre
        );
    }
}
