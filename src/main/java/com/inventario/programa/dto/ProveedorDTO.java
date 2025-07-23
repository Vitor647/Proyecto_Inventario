package com.inventario.programa.dto;

import com.inventario.programa.modelo.Proveedor;

public record ProveedorDTO(Long id, String nombre, String telefono, String email) {

    public static ProveedorDTO fromEntity(Proveedor p) {

        return new ProveedorDTO(p.getId(), p.getNombre(), p.getTelefono(), p.getemail());
    }
}
