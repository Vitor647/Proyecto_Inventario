package com.inventario.programa.servicio;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventario.programa.excepciones.EntidadNoEncontradaExcepcion;
import com.inventario.programa.modelo.Cliente;
import com.inventario.programa.modelo.Vehiculo;
import com.inventario.programa.repositorio.ClienteRepositorio;
import com.inventario.programa.repositorio.VehiculoRepositorio;

@Service
public class VehiculoServicio {

    // --- DTOs para Vehiculo ---
    public record VehiculoListDTO(Long id, String marca, String modelo, String anio, String matricula, Long clienteId, String clienteNombre) {

        public static VehiculoListDTO fromEntity(Vehiculo v) {
            String nombre = (v.getCliente() != null) ? v.getCliente().getNombre() : "Sin asignar";
            Long idCliente = (v.getCliente() != null) ? v.getCliente().getId() : null;
            return new VehiculoListDTO(v.getId(), v.getMarca(), v.getModelo(), String.valueOf(v.getAnio()), v.getMatricula(), idCliente, nombre);
        }
    }

    public record VehiculoInputDTO(String marca, String modelo, String anio, String matricula, Long clienteId) {

    }

    private final VehiculoRepositorio vehiculoRepositorio;
    private final ClienteRepositorio clienteRepositorio;

    public VehiculoServicio(VehiculoRepositorio vehiculoRepositorio, ClienteRepositorio clienteRepositorio) {
        this.vehiculoRepositorio = vehiculoRepositorio;
        this.clienteRepositorio = clienteRepositorio;
    }
    // Dentro de la clase VehiculoServicio

    // --- Métodos del Servicio ---
    @Transactional(readOnly = true)
    public List<VehiculoListDTO> findAll() {
        return vehiculoRepositorio.findAll().stream()
                .map(VehiculoListDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VehiculoListDTO> findByClienteId(Long clienteId) {
        return vehiculoRepositorio.findByClienteId(clienteId).stream()
                .map(VehiculoListDTO::fromEntity)
                .toList();
    }

    @Transactional
    public VehiculoListDTO crearVehiculo(VehiculoInputDTO dto) {
        Cliente cliente = clienteRepositorio.findById(dto.clienteId())
                .orElseThrow(() -> new EntidadNoEncontradaExcepcion("Cliente no encontrado con ID: " + dto.clienteId()));

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMarca(dto.marca());
        vehiculo.setModelo(dto.modelo());
        vehiculo.setAnio(dto.anio());
        vehiculo.setMatricula(dto.matricula());
        vehiculo.setCliente(cliente); // Asociamos el dueño

        Vehiculo guardado = vehiculoRepositorio.save(vehiculo);
        return VehiculoListDTO.fromEntity(guardado);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!vehiculoRepositorio.existsById(id)) {
            throw new EntidadNoEncontradaExcepcion("Vehículo no encontrado con ID: " + id);
        }
        // Aquí podrías añadir lógica para no eliminar si tiene órdenes de trabajo asociadas
        vehiculoRepositorio.deleteById(id);
    }

}
