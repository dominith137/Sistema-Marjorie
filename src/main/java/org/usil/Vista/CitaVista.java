package org.usil.Vista;

import org.usil.Controlador.CitaControlador;
import org.usil.Controlador.ClienteControlador;
import org.usil.Controlador.ServicioControlador;
import org.usil.Controlador.MenuPrincipalControlador;
import org.usil.Modelo.Cita;
import org.usil.Modelo.Cliente;
import org.usil.Modelo.Servicio;
import org.usil.State.EstadoCitaCompletada;
import org.usil.State.EstadoCitaCancelada;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

// Vista para gestionar las citas del sistema
public class CitaVista extends JPanel {
    private CitaControlador controlador;
    private ClienteControlador clienteControlador;
    private ServicioControlador servicioControlador;
    private MenuPrincipalControlador menuControlador;

    private JComboBox<String> comboCliente;
    private JComboBox<String> comboServicio;
    private JTextField txtFecha;
    private JSpinner spinnerHora;
    private JComboBox<String> comboEstado;
    private JTextArea txtObservaciones;
    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnCambiarEstado;
    private JButton btnCancelar;
    private JButton btnVerAgenda;
    private JTextField txtFechaAgenda;

    private Integer citaEditandoId = null;
    private DefaultComboBoxModel<String> modeloClientes;
    private DefaultComboBoxModel<String> modeloServicios;

    public CitaVista(CitaControlador controlador, ClienteControlador clienteControlador, ServicioControlador servicioControlador, MenuPrincipalControlador menuControlador) {
        this.controlador = controlador;
        this.clienteControlador = clienteControlador;
        this.servicioControlador = servicioControlador;
        this.menuControlador = menuControlador;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelPrincipal = new JPanel(new BorderLayout());

        JPanel formPanel = crearFormulario();
        panelPrincipal.add(formPanel, BorderLayout.NORTH);

        JPanel agendaPanel = crearPanelAgenda();
        panelPrincipal.add(agendaPanel, BorderLayout.CENTER);

        add(panelPrincipal);

        actualizarComboClientes();
        actualizarComboServicios();
        actualizarTabla();
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Gestión de Citas"));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        modeloClientes = new DefaultComboBoxModel<>();
        comboCliente = new JComboBox<>(modeloClientes);
        comboCliente.setPreferredSize(new Dimension(200, 25));
        form.add(comboCliente, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Servicio:"), gbc);
        gbc.gridx = 1;
        modeloServicios = new DefaultComboBoxModel<>();
        comboServicio = new JComboBox<>(modeloServicios);
        comboServicio.setPreferredSize(new Dimension(200, 25));
        form.add(comboServicio, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Fecha (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        txtFecha = new JTextField(10);
        txtFecha.setText(LocalDate.now().toString());
        form.add(txtFecha, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        form.add(new JLabel("Hora (HH:MM):"), gbc);
        gbc.gridx = 1;
        SpinnerDateModel horaModel = new SpinnerDateModel();
        spinnerHora = new JSpinner(horaModel);
        JSpinner.DateEditor horaEditor = new JSpinner.DateEditor(spinnerHora, "HH:mm");
        spinnerHora.setEditor(horaEditor);
        spinnerHora.setValue(new java.util.Date());
        spinnerHora.setPreferredSize(new Dimension(100, 25));
        form.add(spinnerHora, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        form.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        // Solo se muestra, no se usa para lógica
        comboEstado = new JComboBox<>(new String[]{"Programada", "Completada", "Cancelada"});
        comboEstado.setEnabled(false);
        form.add(comboEstado, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        form.add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1;
        txtObservaciones = new JTextArea(3, 20);
        txtObservaciones.setLineWrap(true);
        form.add(new JScrollPane(txtObservaciones), gbc);

        JPanel botonesPanel = new JPanel(new FlowLayout());
        btnAgregar = new JButton("Agregar Cita");
        btnEditar = new JButton("Editar Cita");
        btnCambiarEstado = new JButton("Cambiar Estado");
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setEnabled(false);

        botonesPanel.add(btnAgregar);
        botonesPanel.add(btnEditar);
        botonesPanel.add(btnCambiarEstado);
        botonesPanel.add(btnCancelar);

        panel.add(form, BorderLayout.CENTER);
        panel.add(botonesPanel, BorderLayout.SOUTH);

        configurarAccionesBotones();

        return panel;
    }

    private JPanel crearPanelAgenda() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Agenda Diaria"));

        JPanel fechaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fechaPanel.add(new JLabel("Fecha:"));
        txtFechaAgenda = new JTextField(10);
        txtFechaAgenda.setText(LocalDate.now().toString());
        fechaPanel.add(txtFechaAgenda);
        btnVerAgenda = new JButton("Ver Agenda");
        fechaPanel.add(btnVerAgenda);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Hora", "Cliente", "Servicio", "Estado", "Observaciones"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCitas = new JTable(modeloTabla);
        tablaCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(fechaPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaCitas), BorderLayout.CENTER);

        btnVerAgenda.addActionListener(e -> actualizarAgendaDiaria());

        return panel;
    }

    private void configurarAccionesBotones() {
        btnAgregar.addActionListener(e -> {
            if (validarFormulario()) {
                int clienteId = obtenerIdSeleccionado(comboCliente.getSelectedItem().toString());
                int servicioId = obtenerIdSeleccionado(comboServicio.getSelectedItem().toString());
                LocalDate fecha = parsearFecha(txtFecha.getText());
                LocalTime hora = parsearHora(spinnerHora);
                String observaciones = txtObservaciones.getText();

                if (fecha != null && hora != null) {
                    boolean exito = false;
                    if (citaEditandoId != null) {
                        exito = controlador.editarCita(citaEditandoId, clienteId, servicioId, fecha, hora, observaciones);
                        if (exito) {
                            JOptionPane.showMessageDialog(this, "Cita actualizada exitosamente");
                        } else {
                            JOptionPane.showMessageDialog(this, "Error: No se pudo actualizar la cita. Verifique disponibilidad.");
                        }
                    } else {
                        exito = controlador.programarCitaConBuilder(clienteId, servicioId, fecha, hora, observaciones);

                        if (exito) {
                            JOptionPane.showMessageDialog(this, "Cita programada exitosamente");
                        } else {
                            JOptionPane.showMessageDialog(this, "Error: No se pudo programar la cita. Verifique disponibilidad o datos.");
                        }
                    }

                    if (exito) {
                        menuControlador.guardarDatos(); // Guardar automáticamente
                        limpiarFormulario();
                        actualizarTabla();
                        actualizarAgendaDiaria();
                    }
                }
            }
        });

        btnEditar.addActionListener(e -> {
            int filaSeleccionada = tablaCitas.getSelectedRow();
            if (filaSeleccionada != -1) {
                int citaId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
                Cita cita = controlador.buscarCitaPorId(citaId);

                if (cita != null && cita.getEstado() != null && cita.getEstado().permiteModificacion()) {
                    cargarCitaEnFormulario(cita);
                } else {
                    JOptionPane.showMessageDialog(this, "Solo se pueden editar citas programadas");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una cita para editar");
            }
        });

        btnCambiarEstado.addActionListener(e -> {
            int filaSeleccionada = tablaCitas.getSelectedRow();
            if (filaSeleccionada != -1) {
                int citaId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
                Cita cita = controlador.buscarCitaPorId(citaId);

                if (cita != null && cita.getEstado() != null && cita.getEstado().permiteModificacion()) {
                    String[] opciones = {"Completada", "Cancelada"};
                    int opcion = JOptionPane.showOptionDialog(this,
                            "Seleccione el nuevo estado:",
                            "Cambiar Estado",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            opciones,
                            opciones[0]);

                    if (opcion == 0) {
                        controlador.cambiarEstado(citaId, new EstadoCitaCompletada());
                        menuControlador.guardarDatos(); // Guardar automáticamente
                        JOptionPane.showMessageDialog(this, "Cita marcada como completada");
                    } else if (opcion == 1) {
                        controlador.cambiarEstado(citaId, new EstadoCitaCancelada());
                        menuControlador.guardarDatos(); // Guardar automáticamente
                        JOptionPane.showMessageDialog(this, "Cita cancelada");
                    }
                    actualizarTabla();
                    actualizarAgendaDiaria();
                } else {
                    JOptionPane.showMessageDialog(this, "Solo se puede cambiar el estado de citas programadas");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una cita para cambiar su estado");
            }
        });

        btnCancelar.addActionListener(e -> limpiarFormulario());
    }

    private void cargarCitaEnFormulario(Cita cita) {
        citaEditandoId = cita.getId();

        String clienteStr = cita.getCliente().getId() + " - " + cita.getCliente().getNombreCompleto();
        comboCliente.setSelectedItem(clienteStr);

        String servicioStr = cita.getServicio().getId() + " - " + cita.getServicio().getNombre();
        comboServicio.setSelectedItem(servicioStr);

        txtFecha.setText(cita.getFecha().toString());
        java.util.Date fechaHora = java.sql.Timestamp.valueOf(cita.getFecha().atTime(cita.getHora()));
        spinnerHora.setValue(fechaHora);

        if (cita.getEstado() != null) {
            comboEstado.setSelectedItem(cita.getEstado().getNombreEspanol());
        } else {
            comboEstado.setSelectedIndex(0);
        }
        comboEstado.setEnabled(true);

        txtObservaciones.setText(cita.getObservaciones());

        btnAgregar.setText("Guardar Cambios");
        btnEditar.setEnabled(false);
        btnCancelar.setEnabled(true);
    }

    private boolean validarFormulario() {
        if (comboCliente.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente");
            return false;
        }
        if (comboServicio.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un servicio");
            return false;
        }
        if (txtFecha.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una fecha");
            return false;
        }
        return true;
    }

    private LocalDate parsearFecha(String fechaStr) {
        try {
            return LocalDate.parse(fechaStr, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD");
            return null;
        }
    }

    private LocalTime parsearHora(JSpinner spinner) {
        try {
            java.util.Date fecha = (java.util.Date) spinner.getValue();
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(fecha);
            return LocalTime.of(cal.get(java.util.Calendar.HOUR_OF_DAY), cal.get(java.util.Calendar.MINUTE));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al leer la hora");
            return null;
        }
    }

    private int obtenerIdSeleccionado(String item) {
        if (item != null && item.contains(" - ")) {
            return Integer.parseInt(item.split(" - ")[0]);
        }
        return -1;
    }

    private void actualizarComboClientes() {
        modeloClientes.removeAllElements();
        List<Cliente> clientes = clienteControlador.obtenerClientes();
        for (Cliente c : clientes) {
            modeloClientes.addElement(c.getId() + " - " + c.getNombreCompleto());
        }
    }

    private void actualizarComboServicios() {
        modeloServicios.removeAllElements();
        List<Servicio> servicios = servicioControlador.obtenerServiciosActivos();
        for (Servicio s : servicios) {
            modeloServicios.addElement(s.getId() + " - " + s.getNombre());
        }
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        List<Cita> citas = controlador.obtenerTodasLasCitas();
        for (Cita c : citas) {
            modeloTabla.addRow(new Object[]{
                    c.getId(),
                    c.getHora().toString(),
                    c.getNombreCliente(),
                    c.getNombreServicio(),
                    c.getEstado() != null ? c.getEstado().getNombreEspanol() : "N/A",
                    c.getObservaciones()
            });
        }
    }

    private void actualizarAgendaDiaria() {
        LocalDate fecha = parsearFecha(txtFechaAgenda.getText());
        if (fecha != null) {
            modeloTabla.setRowCount(0);
            List<Cita> citas = controlador.obtenerAgendaDiaria(fecha);
            for (Cita c : citas) {
                modeloTabla.addRow(new Object[]{
                        c.getId(),
                        c.getHora().toString(),
                        c.getNombreCliente(),
                        c.getNombreServicio(),
                        c.getEstado() != null ? c.getEstado().getNombreEspanol() : "N/A",
                        c.getObservaciones()
                });
            }
        }
    }

    private void limpiarFormulario() {
        citaEditandoId = null;
        txtFecha.setText(LocalDate.now().toString());
        spinnerHora.setValue(new java.util.Date());
        txtObservaciones.setText("");
        comboEstado.setSelectedIndex(0);
        comboEstado.setEnabled(false);
        btnAgregar.setText("Agregar Cita");
        btnEditar.setEnabled(true);
        btnCancelar.setEnabled(false);
        actualizarComboClientes();
        actualizarComboServicios();
    }

    public void actualizarDatos() {
        actualizarComboClientes();
        actualizarComboServicios();
        actualizarTabla();
        actualizarAgendaDiaria();
    }
}
