package com.inventario.programa.dto;

import com.inventario.programa.modelo.Vehiculo;

public record VehiculoDTO(Long id, String marca, String modelo, String anio, String matricula) {

    public static VehiculoDTO fromEntity(Vehiculo v) {
        return new VehiculoDTO(v.getId(), v.getMarca(), v.getModelo(), v.getAnio(), v.getMatricula());
    }
}
