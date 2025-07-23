package com.inventario.programa.dto;

// Este DTO solo se usa para recibir los datos del frontend al crear un nuevo Proveedor.
public record ProveedorInputDTO(
        String nombre,
        String telefono,
        String email,
        String direccion
        ) {

}
