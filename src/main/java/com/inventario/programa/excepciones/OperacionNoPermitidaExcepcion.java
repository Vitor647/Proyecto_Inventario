package com.inventario.programa.excepciones;

public class OperacionNoPermitidaExcepcion extends RuntimeException {
    public OperacionNoPermitidaExcepcion() {
        super("Operación no permitida.");
    }

    public OperacionNoPermitidaExcepcion(String mensaje) {
        super(mensaje);
    }

    public OperacionNoPermitidaExcepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}