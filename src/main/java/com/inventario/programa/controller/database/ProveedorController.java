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
import org.springframework.web.bind.annotation.RestController;

import com.inventario.programa.dto.ProveedorDTO;
import com.inventario.programa.dto.ProveedorInputDTO;
import com.inventario.programa.excepciones.EntidadDuplicadaExcepcion;
import com.inventario.programa.servicio.ProveedorServicio;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorServicio servicio;

    public ProveedorController(ProveedorServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<ProveedorDTO> listar() {
        return servicio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDTO> buscarPorId(@PathVariable Long id) {
        return servicio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ProveedorInputDTO dto) {
        try {
            ProveedorDTO nuevoProveedor = servicio.save(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProveedor);
        } catch (EntidadDuplicadaExcepcion e) {
            // Devolvemos un error 400 (Bad Request) con un mensaje claro si el nombre ya existe
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody ProveedorInputDTO dto) {
        try {
            return servicio.update(id, dto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (EntidadDuplicadaExcepcion e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        // En un futuro, aquí podrías usar un try-catch para manejar el EntidadNoEncontradaExcepcion
        servicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
