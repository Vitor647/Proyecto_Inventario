// Repositorio para Usuario
package com.inventario.programa.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventario.programa.modelo.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    List<Usuario> findByRol(String rol);

    Usuario findByemail(String email);

    List<Usuario> findByEliminadoEnIsNull();
}
