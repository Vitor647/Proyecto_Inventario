package com.inventario.programa.servicio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventario.programa.dto.OrdenTrabajoDetailDTO;
import com.inventario.programa.dto.OrdenTrabajoInputDTO;
import com.inventario.programa.dto.OrdenTrabajoListDTO;
import com.inventario.programa.excepciones.EntidadNoEncontradaExcepcion;
import com.inventario.programa.modelo.Cliente;
import com.inventario.programa.modelo.OrdenTrabajo;
import com.inventario.programa.modelo.RepuestoUtilizado;
import com.inventario.programa.modelo.Repuestos;
import com.inventario.programa.modelo.Vehiculo;
import com.inventario.programa.repositorio.ClienteRepositorio;
import com.inventario.programa.repositorio.OrdenTrabajoRepositorio;
import com.inventario.programa.repositorio.RepuestoRepositorio;
import com.inventario.programa.repositorio.VehiculoRepositorio;

@Service
public class OrdenTrabajoServicio {

    private final OrdenTrabajoRepositorio ordenRepositorio;
    private final ClienteRepositorio clienteRepositorio;
    private final VehiculoRepositorio vehiculoRepositorio;
    private final RepuestoRepositorio repuestoRepositorio;

    public OrdenTrabajoServicio(OrdenTrabajoRepositorio ordenRepositorio, ClienteRepositorio clienteRepositorio, VehiculoRepositorio vehiculoRepositorio, RepuestoRepositorio repuestoRepositorio) {
        this.ordenRepositorio = ordenRepositorio;
        this.clienteRepositorio = clienteRepositorio;
        this.vehiculoRepositorio = vehiculoRepositorio;
        this.repuestoRepositorio = repuestoRepositorio;
    }

    // En OrdenTrabajoServicio.java
    @Transactional(readOnly = true)
    public List<OrdenTrabajoListDTO> filtrarOrdenes(String estado, String cliente) {

        boolean hasEstado = estado != null && !estado.trim().isEmpty();
        boolean hasCliente = cliente != null && !cliente.trim().isEmpty();

        List<OrdenTrabajo> ordenes;

        if (hasEstado && hasCliente) {

            ordenes = ordenRepositorio.findByEstadoAndClienteNombreWithDetails(OrdenTrabajo.EstadoOrden.valueOf(estado.toUpperCase()), cliente);

        } else if (hasEstado) {

            ordenes = ordenRepositorio.findByEstadoWithDetails(OrdenTrabajo.EstadoOrden.valueOf(estado.toUpperCase()));

        } else if (hasCliente) {

            ordenes = ordenRepositorio.findByClienteNombreWithDetails(cliente);

        } else {

            ordenes = ordenRepositorio.findAllWithClienteAndVehiculo();
        }

        return ordenes.stream().map(OrdenTrabajoListDTO::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public Optional<OrdenTrabajoDetailDTO> obtenerOrdenPorId(Long id) {
        return ordenRepositorio.findById(id).map(OrdenTrabajoDetailDTO::fromEntity);
    }

    @Transactional
    public OrdenTrabajoDetailDTO crearOrden(OrdenTrabajoInputDTO dto) {
        Cliente cliente = clienteRepositorio.findById(dto.clienteId())
                .orElseThrow(() -> new EntidadNoEncontradaExcepcion("Cliente no encontrado con ID: " + dto.clienteId()));
        Vehiculo vehiculo = vehiculoRepositorio.findById(dto.vehiculoId())
                .orElseThrow(() -> new EntidadNoEncontradaExcepcion("Vehículo no encontrado con ID: " + dto.vehiculoId()));

        OrdenTrabajo nuevaOrden = new OrdenTrabajo();
        nuevaOrden.setCliente(cliente);
        nuevaOrden.setVehiculo(vehiculo);
        nuevaOrden.setDescripcionProblema(dto.descripcionProblema());
        nuevaOrden.setEstado(OrdenTrabajo.EstadoOrden.PENDIENTE);
        nuevaOrden.setFechaCreacion(LocalDateTime.now());

        if (dto.repuestos() != null && !dto.repuestos().isEmpty()) {
            List<RepuestoUtilizado> repuestosUtilizados = dto.repuestos().stream().map(repuestoInput -> {
                Repuestos repuesto = repuestoRepositorio.findById(repuestoInput.repuestoId())
                        .orElseThrow(() -> new EntidadNoEncontradaExcepcion("Repuesto no encontrado con ID: " + repuestoInput.repuestoId()));

                RepuestoUtilizado ru = new RepuestoUtilizado();
                ru.setRepuesto(repuesto);
                ru.setCantidad(repuestoInput.cantidad());
                ru.setOrden(nuevaOrden); // Le decimos al "hijo" quién es su "padre"
                return ru;
            }).toList();

            nuevaOrden.setRepuestosUtilizados(repuestosUtilizados); // Añadimos la lista de "hijos" al "padre"
        }

        // JPA se encargará de guardar la orden y todos sus repuestos utilizados juntos.
        OrdenTrabajo ordenFinal = ordenRepositorio.save(nuevaOrden);

        return OrdenTrabajoDetailDTO.fromEntity(ordenFinal);
    }

    @Transactional
    public OrdenTrabajoDetailDTO actualizarEstado(Long id, OrdenTrabajo.EstadoOrden nuevoEstado) {
        OrdenTrabajo orden = ordenRepositorio.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaExcepcion("Orden no encontrada con ID: " + id));

        orden.setEstado(nuevoEstado);

        if (nuevoEstado == OrdenTrabajo.EstadoOrden.COMPLETADA) {

            orden.setFechaCompletado(LocalDateTime.now());
        } else {

            orden.setFechaCompletado(null);
        }

        OrdenTrabajo actualizada = ordenRepositorio.save(orden);
        return OrdenTrabajoDetailDTO.fromEntity(actualizada);
    }

    @Transactional
    public void eliminarOrden(Long id) {
        if (!ordenRepositorio.existsById(id)) {
            throw new EntidadNoEncontradaExcepcion("No se puede eliminar, orden no encontrada con ID: " + id);
        }
        ordenRepositorio.deleteById(id);
    }
}
