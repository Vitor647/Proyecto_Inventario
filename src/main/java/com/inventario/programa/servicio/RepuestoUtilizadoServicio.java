package com.inventario.programa.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventario.programa.dto.RepuestoUtilizadoDTO;
import com.inventario.programa.repositorio.RepuestoUtilizadoRepositorio;

@Service
public class RepuestoUtilizadoServicio {

    private final RepuestoUtilizadoRepositorio repuestoUtilizadoRepositorio;

    public RepuestoUtilizadoServicio(RepuestoUtilizadoRepositorio repuestoUtilizadoRepositorio) {
        this.repuestoUtilizadoRepositorio = repuestoUtilizadoRepositorio;
    }

    @Transactional(readOnly = true)
    public List<RepuestoUtilizadoDTO> obtenerTodos() {
        return repuestoUtilizadoRepositorio.findAll().stream()
                .map(RepuestoUtilizadoDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<RepuestoUtilizadoDTO> obtenerPorId(Long id) {
        return repuestoUtilizadoRepositorio.findById(id)
                .map(RepuestoUtilizadoDTO::fromEntity);
    }

}
