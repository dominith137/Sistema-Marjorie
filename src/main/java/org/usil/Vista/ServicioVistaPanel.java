package org.usil.Vista;

import org.usil.Controlador.ServicioControlador;
import org.usil.Controlador.MenuPrincipalControlador;
import org.usil.Controlador.ResultadoOperacion;
import org.usil.Modelo.Servicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ServicioVistaPanel extends JPanel {
    private ServicioControlador controlador;
    private MenuPrincipalControlador menuControlador;
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

    public ServicioVistaPanel(ServicioControlador controlador, MenuPrincipalControlador menuControlador) {
        this.controlador = controlador;
        this.menuControlador = menuControlador;
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

        btnDesactivar = new JButton("Cambiar Estado");
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
            
            ResultadoOperacion resultado = controlador.validarYProcesarServicio(
                servicioEditandoId, nombre, descripcion, precioStr, duracionStr
            );
            
            if (resultado.esExitoso()) {
                menuControlador.guardarDatos(); // Guardar automáticamente
                actualizarTabla();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, resultado.getMensaje());
            } else {
                JOptionPane.showMessageDialog(this, resultado.getMensaje());
            }
        });

        // Acción del botón Cancelar
        btnCancelar.addActionListener(e -> {
            limpiarFormulario();
        });

        // Acción del botón Cambiar Estado
        btnDesactivar.addActionListener(e -> {
            int filaSeleccionada = tablaServicios.getSelectedRow();
            if (filaSeleccionada != -1) {
                int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
                String nombre = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
                String estadoActual = (String) modeloTabla.getValueAt(filaSeleccionada, 5);
                boolean estaActivo = estadoActual.equals("Activo");
                
                String accion = estaActivo ? "desactivar" : "activar";
                int opcion = JOptionPane.showConfirmDialog(this, 
                    "¿" + accion.substring(0, 1).toUpperCase() + accion.substring(1) + " el servicio '" + nombre + "'?", 
                    "Confirmar", 
                    JOptionPane.YES_NO_OPTION);
                
                if (opcion == JOptionPane.YES_OPTION) {
                    if (controlador.alternarEstadoServicio(id)) {
                        menuControlador.guardarDatos(); // Guardar automáticamente
                        actualizarTabla();
                        String nuevoEstado = estaActivo ? "desactivado" : "activado";
                        JOptionPane.showMessageDialog(this, "Servicio " + nuevoEstado + " exitosamente");
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al cambiar el estado del servicio");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un servicio para cambiar su estado");
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
