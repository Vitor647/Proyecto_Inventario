package com.inventario.programa.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.inventario.programa.modelo.Usuario;
import com.inventario.programa.repositorio.UsuarioRepositorio;

@Service
public class ServicioUsuario {

    private final UsuarioRepositorio usuarioRepositorio;

    public ServicioUsuario(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }

    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepositorio.findById(id);
    }

    public Usuario registrarUsuario(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepositorio.deleteById(id);
    }
}