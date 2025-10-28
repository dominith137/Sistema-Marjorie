package org.usil.Vista;

import org.usil.Controlador.ServicioControlador;
import org.usil.Modelo.Servicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ServicioVistaPanel extends JPanel {
    private ServicioControlador controlador;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtPrecio;
    private JTextField txtDuracion;
    private JTable tablaServicios;
    private DefaultTableModel modeloTabla;

    public ServicioVistaPanel(ServicioControlador controlador) {
        this.controlador = controlador;
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());

        // Formulario
        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        formPanel.add(new JLabel("Nombre del Servicio:"));
        txtNombre = new JTextField();
        formPanel.add(txtNombre);

        formPanel.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextField();
        formPanel.add(txtDescripcion);

        formPanel.add(new JLabel("Precio (S/):"));
        txtPrecio = new JTextField();
        formPanel.add(txtPrecio);

        formPanel.add(new JLabel("Duración (min):"));
        txtDuracion = new JTextField();
        formPanel.add(txtDuracion);

        JButton btnAgregar = new JButton("Agregar Servicio");
        formPanel.add(btnAgregar);

        JButton btnDesactivar = new JButton("Desactivar Servicio");
        formPanel.add(btnDesactivar);

        panel.add(formPanel, BorderLayout.NORTH);

        // Tabla
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripción", "Precio", "Duración", "Estado"}, 0);
        tablaServicios = new JTable(modeloTabla);
        panel.add(new JScrollPane(tablaServicios), BorderLayout.CENTER);

        // Acción del botón Agregar
        btnAgregar.addActionListener(e -> {
            String nombre = txtNombre.getText();
            String descripcion = txtDescripcion.getText();
            String precioStr = txtPrecio.getText();
            String duracionStr = txtDuracion.getText();
            
            if (!nombre.isEmpty() && !precioStr.isEmpty() && !duracionStr.isEmpty()) {
                try {
                    double precio = Double.parseDouble(precioStr);
                    int duracion = Integer.parseInt(duracionStr);
                    
                    if (controlador.agregarServicio(nombre, descripcion, precio, duracion)) {
                        actualizarTabla();
                        txtNombre.setText("");
                        txtDescripcion.setText("");
                        txtPrecio.setText("");
                        txtDuracion.setText("");
                        JOptionPane.showMessageDialog(this, "Servicio agregado exitosamente");
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al agregar servicio");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Complete los campos obligatorios");
            }
        });

        // Acción del botón Desactivar
        btnDesactivar.addActionListener(e -> {
            int filaSeleccionada = tablaServicios.getSelectedRow();
            if (filaSeleccionada != -1) {
                int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
                String nombre = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
                
                int opcion = JOptionPane.showConfirmDialog(this, 
                    "¿Desactivar el servicio '" + nombre + "'?", 
                    "Confirmar", 
                    JOptionPane.YES_NO_OPTION);
                
                if (opcion == JOptionPane.YES_OPTION) {
                    if (controlador.desactivarServicio(id)) {
                        actualizarTabla();
                        JOptionPane.showMessageDialog(this, "Servicio desactivado");
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al desactivar servicio");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un servicio para desactivar");
            }
        });

        add(panel);
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<Servicio> servicios = controlador.obtenerTodosLosServicios();
        for (Servicio s : servicios) {
            modeloTabla.addRow(new Object[]{
                    s.getId(),
                    s.getNombre(),
                    s.getDescripcion(),
                    s.getPrecio(),
                    s.getDuracionMinutos(),
                    s.isActivo() ? "Activo" : "Inactivo"
            });
        }
    }

    public void actualizarDatos() {
        actualizarTabla();
    }
}
