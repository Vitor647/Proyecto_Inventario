package com.inventario.programa.controller.database;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventario.programa.dto.OrdenTrabajoDetailDTO;
import com.inventario.programa.dto.OrdenTrabajoInputDTO;
import com.inventario.programa.dto.OrdenTrabajoListDTO;
import com.inventario.programa.modelo.OrdenTrabajo.EstadoOrden;
import com.inventario.programa.servicio.OrdenTrabajoServicio;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/ordenes")
public class OrdenTrabajoController {

    private final OrdenTrabajoServicio ordenServicio;

    public OrdenTrabajoController(OrdenTrabajoServicio ordenServicio) {
        this.ordenServicio = ordenServicio;
    }

    // DTO específico para recibir la actualización del estado. Es una buena práctica.
    public record EstadoUpdateDTO(String estado) {

    }

    @GetMapping
    public List<OrdenTrabajoListDTO> listarTodas(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String cliente) {
        return ordenServicio.filtrarOrdenes(estado, cliente);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenTrabajoDetailDTO> obtenerPorId(@PathVariable Long id) {
        return ordenServicio.obtenerOrdenPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrdenTrabajoDetailDTO> crear(@RequestBody OrdenTrabajoInputDTO ordenDto) {
        OrdenTrabajoDetailDTO nuevaOrden = ordenServicio.crearOrden(ordenDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaOrden);
    }

    // --- MÉTODO CORREGIDO ---
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody EstadoUpdateDTO dto) {
        try {
            EstadoOrden nuevoEstado = EstadoOrden.valueOf(dto.estado().toUpperCase());
            OrdenTrabajoDetailDTO ordenActualizada = ordenServicio.actualizarEstado(id, nuevoEstado);
            return ResponseEntity.ok(ordenActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("El estado proporcionado no es válido: " + dto.estado());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ordenServicio.eliminarOrden(id);
        return ResponseEntity.noContent().build();
    }
}
