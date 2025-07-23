package com.inventario.programa;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.inventario.programa.modelo.Categoria;
import com.inventario.programa.modelo.Cliente;
import com.inventario.programa.modelo.Proveedor;
import com.inventario.programa.modelo.Usuario;
import com.inventario.programa.modelo.Vehiculo;
import com.inventario.programa.repositorio.CategoriaRepositorio;
import com.inventario.programa.repositorio.ClienteRepositorio;
import com.inventario.programa.repositorio.OrdenTrabajoRepositorio;
import com.inventario.programa.repositorio.ProveedorRepositorio;
import com.inventario.programa.repositorio.RepuestoRepositorio;
import com.inventario.programa.repositorio.UsuarioRepositorio;
import com.inventario.programa.repositorio.VehiculoRepositorio;

@SpringBootApplication
public class ProgramaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProgramaApplication.class, args);
    }

    // --- VERSIÓN FINAL CON TODOS LOS DATOS DE EJEMPLO ---
    @Bean
    public CommandLineRunner loadData(
            CategoriaRepositorio categoriaRepositorio,
            ProveedorRepositorio proveedorRepositorio,
            ClienteRepositorio clienteRepositorio,
            VehiculoRepositorio vehiculoRepositorio,
            OrdenTrabajoRepositorio ordenTrabajoRepositorio,
            UsuarioRepositorio usuarioRepositorio, // Añadimos el repositorio de usuarios
            RepuestoRepositorio repuestoRepositorio) {

        return args -> {
            // --- Cargar Usuario de ejemplo (¡NUEVO!) ---
            if (usuarioRepositorio.count() == 0) {
                System.out.println("Cargando usuario de ejemplo...");
                Usuario admin = new Usuario();
                admin.setNombre("Administrador Principal");
                admin.setemail("admin@taller.com");
                admin.setpassword("admin123");
                admin.setRol(Usuario.Rol.ADMIN);
                usuarioRepositorio.save(admin);
            }

            // --- Cargar Categorías y Proveedores ---
            if (categoriaRepositorio.count() == 0 && proveedorRepositorio.count() == 0) {
                System.out.println("Cargando datos de ejemplo para Categorías y Proveedores...");
                categoriaRepositorio.save(new Categoria("Motor"));
                proveedorRepositorio.save(new Proveedor("Bosch", "911234567"));
            }

            // --- Cargar Clientes y Vehículos ---
            if (clienteRepositorio.count() == 0) {
                System.out.println("Cargando datos de ejemplo para Clientes y Vehículos...");
                Cliente cliente1 = new Cliente("Juan Pérez", "Calle Falsa 123");
                cliente1.setTelefono("600111222");
                cliente1.setEmail("juan.perez@email.com");
                clienteRepositorio.save(cliente1);

                Vehiculo vehiculo1 = new Vehiculo();
                vehiculo1.setMarca("Ford");
                vehiculo1.setModelo("Focus");
                vehiculo1.setAnio("2018");
                vehiculo1.setMatricula("1234ABC");
                vehiculo1.setCliente(cliente1);
                vehiculoRepositorio.save(vehiculo1);
            }
        };
    }
}
