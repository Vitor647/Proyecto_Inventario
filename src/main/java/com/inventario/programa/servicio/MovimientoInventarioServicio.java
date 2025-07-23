package com.inventario.programa.servicio;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventario.programa.dto.MovimientoInventarioDTO;
import com.inventario.programa.dto.MovimientoInventarioInputDTO;
import com.inventario.programa.excepciones.EntidadNoEncontradaExcepcion;
import com.inventario.programa.modelo.Cliente;
import com.inventario.programa.modelo.MovimientoInventario;
import com.inventario.programa.modelo.Repuestos;
import com.inventario.programa.modelo.Usuario;
import com.inventario.programa.repositorio.ClienteRepositorio;
import com.inventario.programa.repositorio.MovimientoRepositorio;
import com.inventario.programa.repositorio.RepuestoRepositorio;
import com.inventario.programa.repositorio.UsuarioRepositorio;

@Service
public class MovimientoInventarioServicio {

    private final MovimientoRepositorio movimientoRepo;
    private final RepuestoRepositorio repuestoRepo;
    private final ClienteRepositorio clienteRepo;
    private final UsuarioRepositorio usuarioRepo;
    private static final Logger logger = LoggerFactory.getLogger(MovimientoInventarioServicio.class);

    public MovimientoInventarioServicio(MovimientoRepositorio movimientoRepo, RepuestoRepositorio repuestoRepo, ClienteRepositorio clienteRepo, UsuarioRepositorio usuarioRepo) {
        this.movimientoRepo = movimientoRepo;
        this.repuestoRepo = repuestoRepo;
        this.clienteRepo = clienteRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> findAll() {
        logger.info("Buscando todos los movimientos de inventario con detalles.");
        // Se llama al método con JOIN FETCH
        return movimientoRepo.findAllWithDetails().stream()
                .map(MovimientoInventarioDTO::fromEntity)
                .toList();
    }

    @Transactional
    public MovimientoInventarioDTO crearMovimiento(MovimientoInventarioInputDTO dto) {
        // ... (el resto del método crearMovimiento se queda como está) ...
        logger.info("Iniciando creación de movimiento con DTO: {}", dto);

        Repuestos repuesto = repuestoRepo.findById(dto.repuestoId())
                .orElseThrow(() -> new EntidadNoEncontradaExcepcion("Repuesto no encontrado con ID: " + dto.repuestoId()));
        logger.info("Repuesto encontrado: {}", repuesto.getNombre());

        MovimientoInventario mov = new MovimientoInventario();
        mov.setRepuestos(repuesto);
        mov.setCantidad(dto.cantidad());
        mov.setTipo(MovimientoInventario.TipoMovimiento.valueOf(dto.tipo().toUpperCase()));
        mov.setFechaMovimiento(LocalDateTime.now());

        if (dto.clienteId() != null) {
            Cliente cliente = clienteRepo.findById(dto.clienteId())
                    .orElseThrow(() -> new EntidadNoEncontradaExcepcion("Cliente no encontrado con ID: " + dto.clienteId()));
            mov.setCliente(cliente);
        }

        if (dto.usuarioId() != null) {
            Usuario usuario = usuarioRepo.findById(dto.usuarioId())
                    .orElseThrow(() -> new EntidadNoEncontradaExcepcion("Usuario no encontrado con ID: " + dto.usuarioId()));
            mov.setUsuario(usuario);
        }

        logger.info("Actualizando stock para el repuesto ID: {}. Stock actual: {}. Cantidad de movimiento: {}", repuesto.getId(), repuesto.getStock(), mov.getCantidad());
        if (mov.getTipo() == MovimientoInventario.TipoMovimiento.ENTRADA) {
            repuesto.setStock(repuesto.getStock() + mov.getCantidad());
            logger.info("Tipo ENTRADA. Nuevo stock: {}", repuesto.getStock());
        } else if (mov.getTipo() == MovimientoInventario.TipoMovimiento.SALIDA) {
            if (repuesto.getStock() < mov.getCantidad()) {
                logger.error("Error de stock: Se intentó sacar {} unidades del repuesto '{}', pero solo hay {}", mov.getCantidad(), repuesto.getNombre(), repuesto.getStock());
                throw new IllegalStateException("No hay suficiente stock para la salida del repuesto: " + repuesto.getNombre());
            }
            repuesto.setStock(repuesto.getStock() - mov.getCantidad());
            logger.info("Tipo SALIDA. Nuevo stock: {}", repuesto.getStock());
        }

        repuestoRepo.save(repuesto);
        MovimientoInventario movGuardado = movimientoRepo.save(mov);

        logger.info("Movimiento de inventario ID: {} creado con éxito.", movGuardado.getId());
        return MovimientoInventarioDTO.fromEntity(movGuardado);
    }
}
