package com.inventario.programa.controller.database;

import java.math.BigDecimal;
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
import org.springframework.web.server.ResponseStatusException;

import com.inventario.programa.dto.RepuestoDTO;
import com.inventario.programa.modelo.Categoria;
import com.inventario.programa.modelo.Proveedor;
import com.inventario.programa.modelo.Repuestos;
import com.inventario.programa.repositorio.CategoriaRepositorio;
import com.inventario.programa.repositorio.ProveedorRepositorio;
import com.inventario.programa.servicio.RepuestoServicio;

@RestController
@RequestMapping("/api/repuestos")
@CrossOrigin(origins = "*") // Considera restringir esto en producción
public class RepuestoController {

    private final RepuestoServicio repuestoServicio;
    private final CategoriaRepositorio categoriaRepositorio;
    private final ProveedorRepositorio proveedorRepositorio;

    public RepuestoController(RepuestoServicio repuestoServicio, CategoriaRepositorio categoriaRepositorio, ProveedorRepositorio proveedorRepositorio) {
        this.repuestoServicio = repuestoServicio;
        this.categoriaRepositorio = categoriaRepositorio;
        this.proveedorRepositorio = proveedorRepositorio;
    }

    @GetMapping
    public List<RepuestoDTO> listar() {
        // Devuelve una lista de DTOs
        return repuestoServicio.listarTodosDTO();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepuestoDTO> obtenerPorId(@PathVariable Long id) {
        // Devuelve un DTO
        return repuestoServicio.obtenerPorIdDTO(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Repuestos no encontrado con ID: " + id));
    }

    @PostMapping
    public ResponseEntity<RepuestoDTO> guardar(@RequestBody RepuestoInputDTO dto) {
        Repuestos nuevoRepuesto = new Repuestos();
        // Mapeo desde el DTO de entrada a la entidad
        mapDtoToEntity(dto, nuevoRepuesto);
        Repuestos guardado = repuestoServicio.guardar(nuevoRepuesto);
        // Devuelve el DTO del repuestos recién creado
        return ResponseEntity.status(HttpStatus.CREATED).body(RepuestoDTO.fromEntity(guardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepuestoDTO> actualizar(@PathVariable Long id, @RequestBody RepuestoInputDTO dto) {
        Repuestos repuestoExistente = repuestoServicio.obtenerPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Repuestos no encontrado con ID: " + id));

        mapDtoToEntity(dto, repuestoExistente);
        Repuestos actualizado = repuestoServicio.guardar(repuestoExistente);
        return ResponseEntity.ok(RepuestoDTO.fromEntity(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        repuestoServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Método ayudante para no repetir código de mapeo
    private void mapDtoToEntity(RepuestoInputDTO dto, Repuestos repuestos) {
        Categoria categoria = categoriaRepositorio.findById(dto.categoriaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría no encontrada con ID: " + dto.categoriaId()));
        Proveedor proveedor = proveedorRepositorio.findById(dto.proveedorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Proveedor no encontrado con ID: " + dto.proveedorId()));

        repuestos.setNombre(dto.nombre());
        repuestos.setDescripcion(dto.descripcion());
        repuestos.setUbicacion(dto.ubicacion());
        repuestos.setStock(dto.stock());
        repuestos.setStockMinimo(dto.stockMinimo());
        repuestos.setPrecio(dto.precio());
        repuestos.setCategoria(categoria);
        repuestos.setProveedor(proveedor);
    }

    // DTO específico para la entrada de datos (POST y PUT)
    public record RepuestoInputDTO(
            String nombre,
            String descripcion,
            String ubicacion,
            int stock,
            int stockMinimo,
            BigDecimal precio,
            Long categoriaId,
            Long proveedorId
            ) {

    }
}
