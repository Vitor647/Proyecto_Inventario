// Repositorio para Cliente
package com.inventario.programa.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventario.programa.modelo.Cliente;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNombreContaining(String nombre);

    List<Cliente> findByEliminadoEnIsNull();

    Optional<Cliente> findByEmail(String email);
}
