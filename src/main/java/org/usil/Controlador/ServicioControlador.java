package org.usil.Controlador;

import org.usil.Modelo.Servicio;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ServicioControlador {
    private final List<Servicio> servicios = new ArrayList<>();
    private int seq = 1;

    // Crear
    public Servicio crearServicio(String nombre, String descripcion, double precio, int duracionMin) {
        Servicio s = new Servicio(seq++, nombre, descripcion, precio, duracionMin);
        servicios.add(s);
        return s;
    }

    // Actualizar
    public boolean actualizarServicio(int id, String nombre, String descripcion, double precio, int duracionMin) {
        Servicio s = buscarPorId(id);
        if (s == null) return false;
        s.setNombre(nombre);
        s.setDescripcion(descripcion);
        s.setPrecio(precio);
        s.setDuracionMin(duracionMin);
        s.validar();
        return true;
    }

    // Desactivar
    public boolean desactivarServicio(int id) {
        Servicio s = buscarPorId(id);
        if (s == null) return false;
        s.setActivo(false);
        return true;
    }

    // Activar
    public boolean activarServicio(int id) {
        Servicio s = buscarPorId(id);
        if (s == null) return false;
        s.setActivo(true);
        return true;
    }

    // Listar
    public List<Servicio> obtenerServicios() {
        return new ArrayList<>(servicios);
    }

    // Buscar por ID
    public Servicio buscarPorId(int id) {
        for (Servicio s : servicios) if (s.getId() == id) return s;
        return null;
    }


}
