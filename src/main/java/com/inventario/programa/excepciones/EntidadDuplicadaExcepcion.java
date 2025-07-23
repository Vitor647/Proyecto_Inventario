package com.inventario.programa.excepciones;
public class EntidadDuplicadaExcepcion extends RuntimeException {
    public EntidadDuplicadaExcepcion() {
       super("Entidad Duplicada.");
    }
    public EntidadDuplicadaExcepcion(String mensaje) {
        super(mensaje);
    } 
    public EntidadDuplicadaExcepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
