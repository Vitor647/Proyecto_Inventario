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

import com.inventario.programa.servicio.CategoriaServicio;
import com.inventario.programa.servicio.CategoriaServicio.CategoriaDTO;
import com.inventario.programa.servicio.CategoriaServicio.CategoriaInputDTO;

@CrossOrigin(origins = "*") // Cambia "*" a tu dominio de frontend en producción
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaServicio categoriaServicio;

    public CategoriaController(CategoriaServicio categoriaServicio) {
        this.categoriaServicio = categoriaServicio;
    }

    @GetMapping
    public List<CategoriaDTO> listar() {
        return categoriaServicio.obtenerTodasCategorias();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> buscarPorId(@PathVariable Long id) {
        return categoriaServicio.obtenerCategoriaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> crear(@RequestBody CategoriaInputDTO categoriaDto) {
        CategoriaDTO nuevaCategoria = categoriaServicio.guardarCategoria(categoriaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> actualizar(@PathVariable Long id, @RequestBody CategoriaInputDTO categoriaDto) {
        CategoriaDTO categoriaActualizada = categoriaServicio.actualizarCategoria(id, categoriaDto);
        return ResponseEntity.ok(categoriaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaServicio.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
