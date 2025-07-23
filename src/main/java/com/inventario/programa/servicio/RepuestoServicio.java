package com.inventario.programa.servicio;

import java.util.List; // <-- IMPORTAR DTO
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.inventario.programa.dto.RepuestoDTO;
import com.inventario.programa.modelo.Repuestos;
import com.inventario.programa.repositorio.RepuestoRepositorio; // <-- IMPORTAR COLLECTORS

@Service
public class RepuestoServicio {

    private static final Logger logger = LoggerFactory.getLogger(RepuestoServicio.class);
    private final RepuestoRepositorio repuestoRepositorio;

    public RepuestoServicio(RepuestoRepositorio repuestoRepositorio) {
        this.repuestoRepositorio = repuestoRepositorio;
    }

    // --- MÉTODOS ACTUALIZADOS PARA DEVOLVER DTOs ---
    public List<RepuestoDTO> listarTodosDTO() {
        return repuestoRepositorio.findByEliminadoEnIsNull().stream()
                .map(RepuestoDTO::fromEntity) // Convierte cada Repuestos a RepuestoDTO
                .collect(Collectors.toList());
    }

    public Optional<RepuestoDTO> obtenerPorIdDTO(Long id) {
        return repuestoRepositorio.findById(id)
                .map(RepuestoDTO::fromEntity); // Convierte el Repuestos encontrado a RepuestoDTO
    }

    // --- MÉTODOS EXISTENTES (USADOS INTERNAMENTE O POR EL CONTROLADOR PARA GUARDAR/ACTUALIZAR) ---
    public Optional<Repuestos> obtenerPorId(Long id) {
        return repuestoRepositorio.findById(id);
    }

    public Repuestos guardar(Repuestos repuestos) {
        logger.info("Datos recibidos para guardar repuestos: {}", repuestos);
        return repuestoRepositorio.save(repuestos);
    }

    public void eliminar(Long id) {
        repuestoRepositorio.findById(id).ifPresent(repuestos -> {
            repuestos.setEliminadoEn(java.time.LocalDateTime.now());
            repuestoRepositorio.save(repuestos);
        });
    }
}
