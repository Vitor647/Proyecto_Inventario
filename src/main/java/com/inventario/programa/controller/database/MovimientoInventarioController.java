package com.inventario.programa.controller.database;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventario.programa.dto.MovimientoInventarioDTO;
import com.inventario.programa.dto.MovimientoInventarioInputDTO;
import com.inventario.programa.excepciones.EntidadNoEncontradaExcepcion;
import com.inventario.programa.servicio.MovimientoInventarioServicio;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/movimientos-inventario")
public class MovimientoInventarioController {

    private final MovimientoInventarioServicio servicio;

    public MovimientoInventarioController(MovimientoInventarioServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<MovimientoInventarioDTO> obtenerTodos() {
        return servicio.findAll();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody MovimientoInventarioInputDTO dto) {
        try {
            MovimientoInventarioDTO nuevoMovimiento = servicio.crearMovimiento(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMovimiento);
        } catch (IllegalStateException | EntidadNoEncontradaExcepcion e) {
            // CORRECCIÓN: Si hay un error conocido, devolvemos un 400 con el mensaje específico.
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Para cualquier otro error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado en el servidor.");
        }
    }
}
