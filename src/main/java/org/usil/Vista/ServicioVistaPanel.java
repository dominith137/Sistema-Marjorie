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
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnDesactivar;
    private JButton btnCancelar;
    private Integer servicioEditandoId = null;

    public ServicioVistaPanel(ServicioControlador controlador) {
        this.controlador = controlador;
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());

        // Formulario
        JPanel formPanel = new JPanel(new GridLayout(7, 2));
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

        btnAgregar = new JButton("Agregar Servicio");
        formPanel.add(btnAgregar);

        btnEditar = new JButton("Editar Servicio");
        formPanel.add(btnEditar);

        btnDesactivar = new JButton("Desactivar Servicio");
        formPanel.add(btnDesactivar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setEnabled(false);
        formPanel.add(btnCancelar);

        panel.add(formPanel, BorderLayout.NORTH);

        // Tabla
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripción", "Precio", "Duración", "Estado"}, 0);
        tablaServicios = new JTable(modeloTabla);
        panel.add(new JScrollPane(tablaServicios), BorderLayout.CENTER);

        // Acción del botón Editar
        btnEditar.addActionListener(e -> {
            int filaSeleccionada = tablaServicios.getSelectedRow();
            if (filaSeleccionada != -1) {
                int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
                Servicio servicio = controlador.buscarServicioPorId(id);
                
                if (servicio != null) {
                    servicioEditandoId = id;
                    txtNombre.setText(servicio.getNombre());
                    txtDescripcion.setText(servicio.getDescripcion());
                    txtPrecio.setText(String.valueOf(servicio.getPrecio()));
                    txtDuracion.setText(String.valueOf(servicio.getDuracionMinutos()));
                    
                    btnAgregar.setText("Guardar Cambios");
                    btnEditar.setEnabled(false);
                    btnDesactivar.setEnabled(false);
                    btnCancelar.setEnabled(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un servicio para editar");
            }
        });

        // Acción del botón Agregar/Guardar
        btnAgregar.addActionListener(e -> {
            String nombre = txtNombre.getText();
            String descripcion = txtDescripcion.getText();
            String precioStr = txtPrecio.getText();
            String duracionStr = txtDuracion.getText();
            
            if (!nombre.isEmpty() && !precioStr.isEmpty() && !duracionStr.isEmpty()) {
                try {
                    double precio = Double.parseDouble(precioStr);
                    int duracion = Integer.parseInt(duracionStr);
                    
                    boolean exito = false;
                    if (servicioEditandoId != null) {
                        // Modo edición
                        exito = controlador.actualizarServicio(servicioEditandoId, nombre, descripcion, precio, duracion);
                        if (exito) {
                            JOptionPane.showMessageDialog(this, "Servicio actualizado exitosamente");
                        }
                    } else {
                        // Modo agregar
                        exito = controlador.agregarServicio(nombre, descripcion, precio, duracion);
                        if (exito) {
                            JOptionPane.showMessageDialog(this, "Servicio agregado exitosamente");
                        }
                    }
                    
                    if (exito) {
                        actualizarTabla();
                        limpiarFormulario();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al procesar el servicio");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Complete los campos obligatorios");
            }
        });

        // Acción del botón Cancelar
        btnCancelar.addActionListener(e -> {
            limpiarFormulario();
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
        
        // Inicializar tabla
        actualizarTabla();
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

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtDuracion.setText("");
        servicioEditandoId = null;
        btnAgregar.setText("Agregar Servicio");
        btnAgregar.setEnabled(true);
        btnEditar.setEnabled(true);
        btnDesactivar.setEnabled(true);
        btnCancelar.setEnabled(false);
    }
}
