package com.inventario.programa.excepciones;

public class InventarioExcepcion extends RuntimeException {
    public InventarioExcepcion() {
        super("Error en el inventario.");
    }
    public InventarioExcepcion(String mensaje) {
        super(mensaje);
    }
    public InventarioExcepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}