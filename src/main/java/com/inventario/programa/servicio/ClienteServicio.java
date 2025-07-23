package com.inventario.programa.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventario.programa.modelo.Cliente;
import com.inventario.programa.modelo.Vehiculo;
import com.inventario.programa.repositorio.ClienteRepositorio;

@Service
public class ClienteServicio {

    // --- DTOs para Cliente ---
    public record ClienteDTO(Long id, String nombre, String telefono, String email) {

        public static ClienteDTO fromEntity(Cliente c) {
            return new ClienteDTO(c.getId(), c.getNombre(), c.getTelefono(), c.getEmail());
        }
    }

    public record VehiculoAsociadoDTO(Long id, String marca, String modelo, String matricula) {

        public static VehiculoAsociadoDTO fromEntity(Vehiculo v) {
            return new VehiculoAsociadoDTO(v.getId(), v.getMarca(), v.getModelo(), v.getMatricula());
        }
    }

    public record ClienteDetailDTO(Long id, String nombre, String direccion, String telefono, String email, List<VehiculoAsociadoDTO> vehiculos) {

        public static ClienteDetailDTO fromEntity(Cliente c) {
            List<VehiculoAsociadoDTO> vehiculosDto = c.getVehiculos().stream().map(VehiculoAsociadoDTO::fromEntity).toList();
            return new ClienteDetailDTO(c.getId(), c.getNombre(), c.getDireccion(), c.getTelefono(), c.getEmail(), vehiculosDto);
        }
    }

    public record ClienteInputDTO(String nombre, String direccion, String telefono, String email) {

    }

    private final ClienteRepositorio clienteRepositorio;

    public ClienteServicio(ClienteRepositorio clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    // --- Métodos del Servicio ---
    @Transactional(readOnly = true)
    public List<ClienteDTO> findAll() {
        return clienteRepositorio.findAll().stream().map(ClienteDTO::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public Optional<ClienteDetailDTO> findDetailsById(Long id) {
        return clienteRepositorio.findById(id).map(ClienteDetailDTO::fromEntity);
    }

    @Transactional
    public ClienteDTO save(ClienteInputDTO clienteDto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(clienteDto.nombre());
        cliente.setDireccion(clienteDto.direccion());
        cliente.setTelefono(clienteDto.telefono());
        cliente.setEmail(clienteDto.email());
        Cliente savedCliente = clienteRepositorio.save(cliente);
        return ClienteDTO.fromEntity(savedCliente);
    }

    @Transactional
    public Optional<ClienteDTO> update(Long id, ClienteInputDTO clienteDto) {
        return clienteRepositorio.findById(id).map(cliente -> {
            cliente.setNombre(clienteDto.nombre());
            cliente.setDireccion(clienteDto.direccion());
            cliente.setTelefono(clienteDto.telefono());
            cliente.setEmail(clienteDto.email());
            Cliente updatedCliente = clienteRepositorio.save(cliente);
            return ClienteDTO.fromEntity(updatedCliente);
        });
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (clienteRepositorio.existsById(id)) {
            clienteRepositorio.deleteById(id);
            return true;
        }
        return false;
    }
}
