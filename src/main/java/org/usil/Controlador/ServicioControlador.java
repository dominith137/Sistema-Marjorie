package org.usil.Controlador;

import org.usil.Modelo.Servicio;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServicioControlador {
    private List<Servicio> listaServicios = new ArrayList<>();
    private int contadorId = 1;

    // Agregar servicio
    public boolean agregarServicio(String nombre, String descripcion, BigDecimal precio, int duracionMinutos) {
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        if (duracionMinutos <= 0) {
            return false;
        }

        Servicio nuevo = new Servicio(contadorId++, nombre.trim(), descripcion, precio, duracionMinutos);
        listaServicios.add(nuevo);
        return true;
    }


    // Obtener todos los servicios (incluyendo inactivos)
    public List<Servicio> obtenerTodosLosServicios() {
        return new ArrayList<>(listaServicios);
    }

    // Buscar servicio por ID
    public Servicio buscarServicioPorId(int id) {
        return listaServicios.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Desactivar servicio
    public boolean desactivarServicio(int id) {
        Servicio servicio = buscarServicioPorId(id);
        if (servicio != null && servicio.isActivo()) {
            servicio.desactivar();
            return true;
        }
        return false;
    }

}
