package org.usil.Controlador;

import org.usil.Modelo.Servicio;
import java.util.ArrayList;
import java.util.List;

public class ServicioControlador {
    private List<Servicio> listaServicios = new ArrayList<>();
    private int contadorId = 1;

    // Agregar servicio
    public boolean agregarServicio(String nombre, String descripcion, double precio, int duracionMinutos) {
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        if (precio <= 0) {
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

    // Actualizar servicio
    public boolean actualizarServicio(int id, String nombre, String descripcion, double precio, int duracionMinutos) {
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        if (precio <= 0) {
            return false;
        }
        if (duracionMinutos <= 0) {
            return false;
        }

        Servicio servicio = buscarServicioPorId(id);
        if (servicio != null) {
            servicio.setNombre(nombre.trim());
            servicio.setDescripcion(descripcion);
            servicio.setPrecio(precio);
            servicio.setDuracionMinutos(duracionMinutos);
            return true;
        }
        return false;
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

    // Alternar estado del servicio (activar/desactivar)
    public boolean alternarEstadoServicio(int id) {
        Servicio servicio = buscarServicioPorId(id);
        if (servicio != null) {
            if (servicio.isActivo()) {
                servicio.desactivar();
            } else {
                servicio.reactivar();
            }
            return true;
        }
        return false;
    }

    // Obtener solo servicios activos
    public List<Servicio> obtenerServiciosActivos() {
        return listaServicios.stream()
                .filter(Servicio::isActivo)
                .toList();
    }

    // Validar y procesar servicio (agregar o actualizar)
    public ResultadoOperacion validarYProcesarServicio(Integer idEdicion, String nombre, String descripcion, String precioStr, String duracionStr) {
        // Validar campos obligatorios
        if (nombre == null || nombre.trim().isEmpty()) {
            return ResultadoOperacion.error("El nombre es obligatorio");
        }
        if (precioStr == null || precioStr.trim().isEmpty()) {
            return ResultadoOperacion.error("El precio es obligatorio");
        }
        if (duracionStr == null || duracionStr.trim().isEmpty()) {
            return ResultadoOperacion.error("La duración es obligatoria");
        }

        // Parsear valores numéricos
        double precio;
        int duracion;
        try {
            precio = Double.parseDouble(precioStr.trim());
            duracion = Integer.parseInt(duracionStr.trim());
        } catch (NumberFormatException e) {
            return ResultadoOperacion.error("Ingrese valores numéricos válidos");
        }

        // Validar valores
        if (precio <= 0) {
            return ResultadoOperacion.error("El precio debe ser mayor a cero");
        }
        if (duracion <= 0) {
            return ResultadoOperacion.error("La duración debe ser mayor a cero");
        }

        // Procesar según modo
        boolean exito;
        if (idEdicion != null) {
            exito = actualizarServicio(idEdicion, nombre.trim(), descripcion, precio, duracion);
            return exito ? ResultadoOperacion.exito("Servicio actualizado exitosamente") 
                         : ResultadoOperacion.error("Error al actualizar el servicio");
        } else {
            exito = agregarServicio(nombre.trim(), descripcion, precio, duracion);
            return exito ? ResultadoOperacion.exito("Servicio agregado exitosamente") 
                         : ResultadoOperacion.error("Error al agregar el servicio");
        }
    }
}
