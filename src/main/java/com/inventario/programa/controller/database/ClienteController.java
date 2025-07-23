package com.inventario.programa.controller.database;

// Imports necesarios para los DTOs
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

import com.inventario.programa.servicio.ClienteServicio;
import com.inventario.programa.servicio.ClienteServicio.ClienteDTO;
import com.inventario.programa.servicio.ClienteServicio.ClienteDetailDTO;
import com.inventario.programa.servicio.ClienteServicio.ClienteInputDTO;

@CrossOrigin(origins = "*") // Usamos "*" para máxima compatibilidad en desarrollo
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteServicio clienteServicio;

    public ClienteController(ClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;
    }

    @GetMapping
    public List<ClienteDTO> listar() {
        // CORRECCIÓN: Llamamos al nuevo método findAll() que devuelve una lista de DTOs
        return clienteServicio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDetailDTO> buscarPorId(@PathVariable Long id) {
        // CORRECCIÓN: Llamamos al método que devuelve el DTO con detalles (incluyendo vehículos)
        return clienteServicio.findDetailsById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> crear(@RequestBody ClienteInputDTO clienteDto) {
        // CORRECCIÓN: El método ahora recibe un ClienteInputDTO
        ClienteDTO nuevoCliente = clienteServicio.save(clienteDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizar(@PathVariable Long id, @RequestBody ClienteInputDTO clienteDto) {
        // CORRECCIÓN: El método ahora recibe un ClienteInputDTO y el ID
        return clienteServicio.update(id, clienteDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        // CORRECCIÓN: Llamamos al nuevo método deleteById
        boolean eliminado = clienteServicio.deleteById(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
