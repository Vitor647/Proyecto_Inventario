package com.inventario.programa.controller.database;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventario.programa.servicio.VehiculoServicio;
import com.inventario.programa.servicio.VehiculoServicio.VehiculoInputDTO;
import com.inventario.programa.servicio.VehiculoServicio.VehiculoListDTO;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoServicio vehiculoServicio;

    public VehiculoController(VehiculoServicio vehiculoServicio) {
        this.vehiculoServicio = vehiculoServicio;
    }

    // Endpoint para la vista global de vehículos
    @GetMapping
    public List<VehiculoListDTO> listarTodos() {
        return vehiculoServicio.findAll();
    }

    // Endpoint para el formulario de "Nueva Orden", para encontrar vehículos de un cliente
    @GetMapping("/cliente/{clienteId}")
    public List<VehiculoListDTO> obtenerVehiculosPorCliente(@PathVariable Long clienteId) {
        return vehiculoServicio.findByClienteId(clienteId);
    }

    // Endpoint para el formulario de "Nuevo Vehículo"
    @PostMapping
    public ResponseEntity<VehiculoListDTO> crear(@RequestBody VehiculoInputDTO dto) {
        VehiculoListDTO nuevoVehiculo = vehiculoServicio.crearVehiculo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoVehiculo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vehiculoServicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
