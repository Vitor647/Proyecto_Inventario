package com.inventario.programa.dto;

import com.inventario.programa.modelo.Categoria;

public record CategoriaDTO(Long id, String nombre) {

    public static CategoriaDTO fromEntity(Categoria c) {
        return new CategoriaDTO(c.getId(), c.getNombre());
    }
}
