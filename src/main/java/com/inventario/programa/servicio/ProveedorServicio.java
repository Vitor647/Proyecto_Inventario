package com.inventario.programa.servicio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventario.programa.dto.ProveedorDTO;
import com.inventario.programa.dto.ProveedorInputDTO;
import com.inventario.programa.excepciones.EntidadDuplicadaExcepcion;
import com.inventario.programa.excepciones.EntidadNoEncontradaExcepcion;
import com.inventario.programa.modelo.Proveedor;
import com.inventario.programa.repositorio.ProveedorRepositorio;

@Service
public class ProveedorServicio {

    private final ProveedorRepositorio repositorio;

    public ProveedorServicio(ProveedorRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public List<ProveedorDTO> findAll() {
        return repositorio.findByEliminadoEnIsNull().stream()
                .map(ProveedorDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ProveedorDTO> findById(Long id) {
        return repositorio.findById(id).map(ProveedorDTO::fromEntity);
    }

    @Transactional
    public ProveedorDTO save(ProveedorInputDTO dto) {
        // --- VALIDACIÓN CLAVE PARA EVITAR DUPLICADOS ---
        if (repositorio.existsByNombre(dto.nombre())) {
            throw new EntidadDuplicadaExcepcion("Ya existe un proveedor con el nombre: " + dto.nombre());
        }

        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(dto.nombre());
        proveedor.setTelefono(dto.telefono());
        proveedor.setemail(dto.email()); // Usamos 'email' como estándar
        proveedor.setDireccion(dto.direccion());

        Proveedor guardado = repositorio.save(proveedor);
        return ProveedorDTO.fromEntity(guardado);
    }

    @Transactional
    public Optional<ProveedorDTO> update(Long id, ProveedorInputDTO dto) {
        return repositorio.findById(id).map(existente -> {
            // Comprueba si el nuevo nombre ya lo tiene OTRO proveedor
            Optional<Proveedor> proveedorConMismoNombre = repositorio.findByNombre(dto.nombre());
            if (proveedorConMismoNombre.isPresent() && !proveedorConMismoNombre.get().getId().equals(id)) {
                throw new EntidadDuplicadaExcepcion("Ya existe otro proveedor con el nombre: " + dto.nombre());
            }

            existente.setNombre(dto.nombre());
            existente.setTelefono(dto.telefono());
            existente.setemail(dto.email());
            existente.setDireccion(dto.direccion());

            Proveedor actualizado = repositorio.save(existente);
            return ProveedorDTO.fromEntity(actualizado);
        });
    }

    @Transactional
    public void deleteById(Long id) {
        Proveedor proveedor = repositorio.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaExcepcion("Proveedor no encontrado con ID: " + id));

        // Lógica de borrado suave (soft delete)
        proveedor.setEliminadoEn(LocalDateTime.now());
        repositorio.save(proveedor);
    }
}
