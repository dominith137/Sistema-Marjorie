package org.usil.Vista;

import org.usil.Controlador.ServicioControlador;
import org.usil.Modelo.Servicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ServicioVista extends JFrame {
    private final ServicioControlador controlador;

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JTextField txtPrecio;
    private JTextField txtDuracion;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel lblImagen;

    public ServicioVista(ServicioControlador controlador) {
        this.controlador = controlador;
        setTitle("Gestión de Servicios");
        setSize(980, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Datos"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        txtId = new JTextField(); txtId.setEnabled(false);
        txtNombre = new JTextField();
        txtDescripcion = new JTextArea(3, 30);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtPrecio = new JTextField();
        txtDuracion = new JTextField();

        int row = 0;
        c.gridx=0; c.gridy=row; form.add(new JLabel("ID:"), c);
        c.gridx=1; form.add(txtId, c); row++;

        c.gridx=0; c.gridy=row; form.add(new JLabel("Nombre:"), c);
        c.gridx=1; form.add(txtNombre, c); row++;

        c.gridx=0; c.gridy=row; form.add(new JLabel("Descripción:"), c);
        c.gridx=1; form.add(new JScrollPane(txtDescripcion), c); row++;

        c.gridx=0; c.gridy=row; form.add(new JLabel("Precio (S/):"), c);
        c.gridx=1; form.add(txtPrecio, c); row++;

        c.gridx=0; c.gridy=row; form.add(new JLabel("Duración (min):"), c);
        c.gridx=1; form.add(txtDuracion, c); row++;

        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnCrear = new JButton("Crear");
        JButton btnActualizar = new JButton("Actualizar");

        JPanel barraAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        barraAcciones.add(btnLimpiar);
        barraAcciones.add(btnCrear);
        barraAcciones.add(btnActualizar);
        barraAcciones.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));

        lblImagen = new JLabel("", SwingConstants.CENTER);
        lblImagen.setPreferredSize(new Dimension(340, 200));
        lblImagen.setOpaque(true);
        lblImagen.setBackground(new Color(245,245,245));
        lblImagen.setBorder(BorderFactory.createTitledBorder(""));

        try {
            java.net.URL url = new java.net.URL("https://scontent.flim2-6.fna.fbcdn.net/v/t39.30808-6/481193315_1149180923366140_5455277713738691539_n.jpg?_nc_cat=107&ccb=1-7&_nc_sid=cc71e4&_nc_eui2=AeFDBrLI5cxSOablVBGXbV5RJduZHfOTnY0l25kd85OdjUYVNDSMCLUxhMr9N9DuQ1AuoAYcxz4mSOtkR19l_w96&_nc_ohc=hspEvmMpxI4Q7kNvwFY2Fwd&_nc_oc=AdnJlfbzPagB6XM1UyJoe73pEmF63DocWnQg7yWNZe12vGZ4O2u7z0oSC82W_HI5F4C5OSlYVojWn4uy9pA3feVN&_nc_zt=23&_nc_ht=scontent.flim2-6.fna&_nc_gid=ENsCbkYp0g05vt4qZWEMpw&oh=00_AfeqESB1v05QOc3nj9Me6NNPOnKlr4aJeVJLO6-7xHdjjg&oe=690617A0");
            java.awt.Image img = javax.imageio.ImageIO.read(url);
            int targetW = lblImagen.getPreferredSize().width;
            int targetH = lblImagen.getPreferredSize().height;
            int iw = img.getWidth(null), ih = img.getHeight(null);
            double r = Math.min((double) targetW / iw, (double) targetH / ih);
            int nw = Math.max(1, (int) Math.round(iw * r));
            int nh = Math.max(1, (int) Math.round(ih * r));
            java.awt.Image scaled = img.getScaledInstance(nw, nh, java.awt.Image.SCALE_SMOOTH);
            lblImagen.setIcon(new ImageIcon(scaled));
            lblImagen.setText(null);
        } catch (Exception ex) {
            lblImagen.setText("No se pudo cargar la imagen");
        }

        JPanel lateral = new JPanel(new BorderLayout(8,8));
        lateral.add(barraAcciones, BorderLayout.NORTH);
        lateral.add(lblImagen, BorderLayout.CENTER);

        JPanel top = new JPanel(new BorderLayout(10,10));
        top.add(form, BorderLayout.CENTER);
        top.add(lateral, BorderLayout.EAST);

        // ===== Tabla con Estado =====
        String[] cols = {"ID","Nombre","Descripción","Precio","Duración","Estado"};
        modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int col){ return false; }
            @Override public Class<?> getColumnClass(int col){
                return col == 5 ? Boolean.class : Object.class; // para mostrar checkbox
            }
        };
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        JButton btnDesactivar = new JButton("Desactivar");
        JButton btnActivar   = new JButton("Activar");

        JPanel barraTabla = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        barraTabla.add(btnActivar);
        barraTabla.add(btnDesactivar);
        barraTabla.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));

        JPanel centro = new JPanel(new BorderLayout(5,5));
        centro.add(barraTabla, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);

        JPanel accionesInferiores = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JPanel content = new JPanel(new BorderLayout(10,10));
        content.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        content.add(top, BorderLayout.NORTH);
        content.add(centro, BorderLayout.CENTER);
        content.add(accionesInferiores, BorderLayout.SOUTH);
        setContentPane(content);

        btnCrear.addActionListener(e -> crear());
        btnActualizar.addActionListener(e -> actualizar());
        btnLimpiar.addActionListener(e -> limpiar());

        btnDesactivar.addActionListener(e -> desactivar());
        btnActivar.addActionListener(e -> activar());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarDesdeTabla();
        });

        refrescarTabla();
    }

    private void crear() {
        try {
            String nombre = txtNombre.getText();
            String desc = txtDescripcion.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            int dur = Integer.parseInt(txtDuracion.getText());
            Servicio s = controlador.crearServicio(nombre, desc, precio, dur);
            refrescarTabla();
            seleccionarFilaPorId(s.getId());
            JOptionPane.showMessageDialog(this, "Servicio creado (ID " + s.getId() + ")");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio/Duración inválidos");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void actualizar() {
        try {
            if (txtId.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Selecciona un servicio en la tabla");
                return;
            }
            int id = Integer.parseInt(txtId.getText());
            String nombre = txtNombre.getText();
            String desc = txtDescripcion.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            int dur = Integer.parseInt(txtDuracion.getText());
            if (controlador.actualizarServicio(id, nombre, desc, precio, dur)) {
                refrescarTabla();
                seleccionarFilaPorId(id);
                JOptionPane.showMessageDialog(this, "Servicio actualizado");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio/Duración inválidos");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void desactivar() {
        if (txtId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecciona un servicio");
            return;
        }
        int id = Integer.parseInt(txtId.getText());
        if (controlador.desactivarServicio(id)) {
            refrescarTabla();
            seleccionarFilaPorId(id);
            JOptionPane.showMessageDialog(this, "Servicio desactivado");
        }
    }

    private void activar() {
        if (txtId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecciona un servicio");
            return;
        }
        int id = Integer.parseInt(txtId.getText());
        if (controlador.activarServicio(id)) {
            refrescarTabla();
            seleccionarFilaPorId(id);
            JOptionPane.showMessageDialog(this, "Servicio activado");
        }
    }

    private void cargarDesdeTabla() {
        int row = tabla.getSelectedRow();
        if (row < 0) return;
        txtId.setText(String.valueOf(modelo.getValueAt(row, 0)));
        txtNombre.setText(String.valueOf(modelo.getValueAt(row, 1)));
        txtDescripcion.setText(String.valueOf(modelo.getValueAt(row, 2)));
        txtPrecio.setText(String.valueOf(modelo.getValueAt(row, 3)));
        txtDuracion.setText(String.valueOf(modelo.getValueAt(row, 4)));
        // Estado solo se muestra en la tabla
    }

    private void refrescarTabla() {
        List<Servicio> lista = controlador.obtenerServicios();
        modelo.setRowCount(0);
        for (Servicio s : lista) {
            modelo.addRow(new Object[]{
                    s.getId(),
                    s.getNombre(),
                    s.getDescripcion(),
                    s.getPrecio(),
                    s.getDuracionMin(),
                    s.isActivo()
            });
        }
    }

    private void seleccionarFilaPorId(int id) {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object val = modelo.getValueAt(i, 0);
            if (val instanceof Integer && (int)val == id) {
                tabla.getSelectionModel().setSelectionInterval(i, i);
                tabla.scrollRectToVisible(tabla.getCellRect(i, 0, true));
                break;
            } else if (String.valueOf(val).equals(String.valueOf(id))) {
                tabla.getSelectionModel().setSelectionInterval(i, i);
                tabla.scrollRectToVisible(tabla.getCellRect(i, 0, true));
                break;
            }
        }
    }

    private void limpiar() {
        txtId.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtDuracion.setText("");
        tabla.clearSelection();
    }

    public void mostrar() { setVisible(true); }
}
