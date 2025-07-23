package com.inventario.programa.excepciones;

public class EntidadNoEncontradaExcepcion extends RuntimeException {
    public EntidadNoEncontradaExcepcion() {
        super("Entidad no encontrada.");
    }
    public EntidadNoEncontradaExcepcion(String mensaje) {
        super(mensaje);
    }
    public EntidadNoEncontradaExcepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
