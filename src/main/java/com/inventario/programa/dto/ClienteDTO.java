package com.inventario.programa.dto;

import com.inventario.programa.modelo.Cliente;

public record ClienteDTO(Long id, String nombre, String direccion, String telefono, String email) {

    public static ClienteDTO fromEntity(Cliente c) {
        return new ClienteDTO(c.getId(), c.getNombre(), c.getDireccion(), c.getTelefono(), c.getEmail());
    }
}
