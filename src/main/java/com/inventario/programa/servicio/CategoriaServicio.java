package com.inventario.programa.servicio;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventario.programa.excepciones.EntidadDuplicadaExcepcion;
import com.inventario.programa.excepciones.OperacionNoPermitidaExcepcion;
import com.inventario.programa.modelo.Categoria;
import com.inventario.programa.repositorio.CategoriaRepositorio;

@Service
public class CategoriaServicio {

    private final CategoriaRepositorio categoriaRepositorio;

    // DTO para la salida de datos (lo que se envía al frontend)
    public record CategoriaDTO(Long id, String nombre, String descripcion) {

        public static CategoriaDTO fromEntity(Categoria categoria) {
            return new CategoriaDTO(categoria.getId(), categoria.getNombre(), categoria.getDescripcion());
        }
    }

    // DTO para la entrada de datos (lo que se recibe del frontend para crear/actualizar)
    public record CategoriaInputDTO(String nombre, String descripcion) {

    }

    public CategoriaServicio(CategoriaRepositorio categoriaRepositorio) {
        this.categoriaRepositorio = categoriaRepositorio;
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> obtenerTodasCategorias() {
        return categoriaRepositorio.findAll().stream()
                .map(CategoriaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CategoriaDTO> obtenerCategoriaPorId(Long id) {
        return categoriaRepositorio.findById(id).map(CategoriaDTO::fromEntity);
    }

    @Transactional
    public CategoriaDTO guardarCategoria(CategoriaInputDTO categoriaDto) {
        if (categoriaRepositorio.existsByNombre(categoriaDto.nombre())) {
            throw new EntidadDuplicadaExcepcion("Ya existe una categoría con el nombre: " + categoriaDto.nombre());
        }
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre(categoriaDto.nombre());
        nuevaCategoria.setDescripcion(categoriaDto.descripcion());

        Categoria guardada = categoriaRepositorio.save(nuevaCategoria);
        return CategoriaDTO.fromEntity(guardada);
    }

    @Transactional
    public CategoriaDTO actualizarCategoria(Long id, CategoriaInputDTO categoriaDto) {
        Categoria existente = categoriaRepositorio.findById(id)
                .orElseThrow(() -> new OperacionNoPermitidaExcepcion("Categoría no encontrada con ID: " + id));

        // Valida si el nuevo nombre ya lo tiene OTRA categoría
        Optional<Categoria> categoriaConMismoNombre = categoriaRepositorio.findByNombre(categoriaDto.nombre());
        if (categoriaConMismoNombre.isPresent() && !categoriaConMismoNombre.get().getId().equals(id)) {
            throw new EntidadDuplicadaExcepcion("Ya existe otra categoría con el nombre: " + categoriaDto.nombre());
        }

        existente.setNombre(categoriaDto.nombre());
        existente.setDescripcion(categoriaDto.descripcion());

        Categoria actualizada = categoriaRepositorio.save(existente);
        return CategoriaDTO.fromEntity(actualizada);
    }

    @Transactional
    public void eliminarCategoria(Long id) {
        if (categoriaRepositorio.countRepuestosByCategoriaId(id) > 0) {
            throw new OperacionNoPermitidaExcepcion("No se puede eliminar la categoría porque tiene repuestos asociados.");
        }
        categoriaRepositorio.deleteById(id);
    }
}
