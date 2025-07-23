package com.inventario.programa.dto;

import java.math.BigDecimal;

import com.inventario.programa.modelo.Repuestos;

// Añade String descripcion y String ubicacion a la definición
public record RepuestoDTO(Long id, String nombre, String descripcion, String ubicacion, int stock, BigDecimal precio, String categoriaNombre, String proveedorNombre) {

    public static RepuestoDTO fromEntity(Repuestos r) {
        return new RepuestoDTO(
                r.getId(),
                r.getNombre(),
                r.getDescripcion(),
                r.getUbicacion(),
                r.getStock(),
                r.getPrecio(),
                r.getCategoria().getNombre(),
                r.getProveedor().getNombre()
        );
    }
}
