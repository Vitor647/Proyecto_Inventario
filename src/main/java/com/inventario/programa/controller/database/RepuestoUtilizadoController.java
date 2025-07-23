package com.inventario.programa.controller.database;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventario.programa.dto.RepuestoUtilizadoDTO;
import com.inventario.programa.servicio.RepuestoUtilizadoServicio;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/repuestos-utilizados")
public class RepuestoUtilizadoController {

    private final RepuestoUtilizadoServicio repuestoUtilizadoServicio;

    public RepuestoUtilizadoController(RepuestoUtilizadoServicio repuestoUtilizadoServicio) {
        this.repuestoUtilizadoServicio = repuestoUtilizadoServicio;
    }

    @GetMapping
    public List<RepuestoUtilizadoDTO> obtenerTodos() {
        return repuestoUtilizadoServicio.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepuestoUtilizadoDTO> obtenerPorId(@PathVariable Long id) {
        return repuestoUtilizadoServicio.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
