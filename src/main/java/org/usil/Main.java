package org.usil;

import org.usil.Controlador.MenuPrincipalControlador;
import org.usil.Vista.MenuPrincipalVista;

public class Main {
    public static void main(String[] args) {
        // Crear controlador principal
        MenuPrincipalControlador controlador = new MenuPrincipalControlador();
        
        // Crear y mostrar vista principal
        MenuPrincipalVista vista = new MenuPrincipalVista(controlador);
        vista.mostrar();
    }
}
