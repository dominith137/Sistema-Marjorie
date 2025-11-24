package org.usil.Singleton;

public class GestorDatos {
    // 1. Instancia única (estática)
    private static GestorDatos instancia;

    // 2. Constructor privado (evita new desde fuera)
    private GestorDatos() {
        // inicialización si es necesaria
    }

    // 3. Método público para obtener la instancia
    public static GestorDatos getInstancia() {
        if (instancia == null) {
            instancia = new GestorDatos();
        }
        return instancia;
    }

    // 4. Métodos de gestión de datos (ejemplo)
    public void guardar(Object objeto) {
        System.out.println("Guardando: " + objeto);
    }

    public void eliminar(Object objeto) {
        System.out.println("Eliminando: " + objeto);
    }
}

