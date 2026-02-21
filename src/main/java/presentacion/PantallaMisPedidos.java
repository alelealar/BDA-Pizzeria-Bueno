package presentacion;

import Negocio.BOs.IPizzaBO;
import persistencia.dominio.Pedido;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import persistencia.excepciones.PersistenciaException;

public class PantallaMisPedidos extends JFrame {

    private final IPizzaBO pizzaBO;

    private JTable tabla;
    private DefaultTableModel modelo;
    private List<Pedido> listaPedidos;

    private JComboBox<String> comboEstado;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;

    private final Font FONT_LOGO = new Font("Segoe UI", Font.BOLD, 34);
    private final Font FONT_MENU = new Font("Segoe UI", Font.PLAIN, 16);

    public PantallaMisPedidos(IPizzaBO pizzaBO) {
        this.pizzaBO = pizzaBO;

        setTitle("Papizza - Mis Pedidos");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        crearHeader();

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(new Color(245, 245, 245));

        contenido.add(crearPanelFiltros(), BorderLayout.NORTH);
        contenido.add(crearTabla(), BorderLayout.CENTER);

        add(contenido, BorderLayout.CENTER);

        inicializarDatos();
        cargarDatosTabla(listaPedidos);
    }

    // HEADER

    private void crearHeader() {

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(new Color(138, 37, 22));
        barraSuperior.setPreferredSize(new Dimension(1000, 80));

        JLabel lblLogo = new JLabel("PAPIZZA");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(FONT_LOGO);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 0));

        barraSuperior.add(lblLogo, BorderLayout.WEST);

        JPanel barraInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 12));
        barraInferior.setBackground(new Color(227, 101, 57));
        barraInferior.setPreferredSize(new Dimension(1000, 55));

        barraInferior.add(crearBotonMenu("Inicio"));
        barraInferior.add(crearBotonMenu("Actualizar"));
        barraInferior.add(crearBotonMenu("Carrito"));
        barraInferior.add(crearBotonMenu("Mis pedidos"));

        header.add(barraSuperior);
        header.add(barraInferior);

        add(header, BorderLayout.NORTH);
    }

    private JButton crearBotonMenu(String texto) {

        JButton btn = new JButton(texto);
        btn.setFont(FONT_MENU);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(227, 101, 57));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25));

        btn.addActionListener(e -> {

            switch (texto) {

                case "Carrito":
                    new PantallaCarrito(pizzaBO).setVisible(true);
                    dispose();
                    break;

                case "Inicio":
                {
                    try {
                        new PantallaInicioPedidoProgramado(pizzaBO).setVisible(true);
                    } catch (PersistenciaException ex) {
                        Logger.getLogger(PantallaMisPedidos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    dispose();
                    break;


                case "Actualizar":
                    JOptionPane.showMessageDialog(this,
                            "Función actualizar aún no implementada.");
                    break;

                case "Mis pedidos":
                    break;
            }
        });

        return btn;
    }

    // DATOS 

    private void inicializarDatos() {

        listaPedidos = new ArrayList<>();

        listaPedidos.add(new Pedido(1001, "Sin cebolla", "PENDIENTE",
                LocalDateTime.of(2026, 2, 14, 10, 30),
                LocalDateTime.of(2026, 2, 15, 12, 0)));

        listaPedidos.add(new Pedido(1002, "Extra queso", "ENTREGADO",
                LocalDateTime.of(2026, 2, 10, 12, 0),
                LocalDateTime.of(2026, 2, 10, 13, 0)));

        listaPedidos.add(new Pedido(1003, "Sin piña", "CANCELADO",
                LocalDateTime.of(2026, 2, 5, 9, 0),
                LocalDateTime.of(2026, 2, 6, 11, 0)));
    }

    // FILTROS 

    private JPanel crearPanelFiltros() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        panel.setBackground(new Color(245, 245, 245));

        comboEstado = new JComboBox<>(new String[]{
                "TODOS", "PENDIENTE", "ENTREGADO", "CANCELADO"
        });

        txtFechaInicio = new JTextField(10);
        txtFechaFin = new JTextField(10);

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setBackground(new Color(255, 94, 58));
        btnFiltrar.setForeground(Color.WHITE);

        btnFiltrar.addActionListener(e -> aplicarFiltros());

        panel.add(new JLabel("Estado:"));
        panel.add(comboEstado);
        panel.add(new JLabel("Fecha Inicio (yyyy-MM-dd):"));
        panel.add(txtFechaInicio);
        panel.add(new JLabel("Fecha Fin (yyyy-MM-dd):"));
        panel.add(txtFechaFin);
        panel.add(btnFiltrar);

        return panel;
    }

    private void aplicarFiltros() {

        List<Pedido> filtrados = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime inicio = null;
        LocalDateTime fin = null;

        try {
            if (!txtFechaInicio.getText().isEmpty()){
                LocalDate fechaInicio = LocalDate.parse(txtFechaInicio.getText(), formatter);
                inicio = fechaInicio.atStartOfDay();
            }

            if (!txtFechaFin.getText().isEmpty()){
                LocalDate fechaFin = LocalDate.parse(txtFechaFin.getText(), formatter);
                fin = fechaFin.atTime(23, 59, 59);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto");
            return;
        }

        String estadoSel = comboEstado.getSelectedItem().toString();

        for (Pedido p : listaPedidos) {

            boolean estadoOK = estadoSel.equals("TODOS")
                    || p.getEstadoActual().equals(estadoSel);

            boolean fechaOK = true;

            if (inicio != null && p.getFechaHoraPedido().isBefore(inicio))
                fechaOK = false;

            if (fin != null && p.getFechaHoraPedido().isAfter(fin))
                fechaOK = false;

            if (estadoOK && fechaOK)
                filtrados.add(p);
        }

        cargarDatosTabla(filtrados);
    }

    // TABLA

    private JScrollPane crearTabla() {

        modelo = new DefaultTableModel(
                new Object[]{"Folio", "Nota", "Estado", "Fecha Pedido", "Fecha Entrega", "Acción"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(45);

        tabla.getColumn("Acción").setCellRenderer(new ButtonRenderer());
        tabla.getColumn("Acción").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        return scroll;
    }

    private void cargarDatosTabla(List<Pedido> lista) {

        modelo.setRowCount(0);

        for (Pedido p : lista) {

            modelo.addRow(new Object[]{
                    p.getIdPedido(),
                    p.getNota(),
                    p.getEstadoActual(),
                    p.getFechaHoraPedido(),
                    p.getFechaHoraEntrega(),
                    "Cancelar"
            });
        }
    }

    
    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {

            Pedido p = listaPedidos.get(table.convertRowIndexToModel(row));

            setText("Cancelar");

            if (p.getEstadoActual().equals("PENDIENTE")) {
                setEnabled(true);
                setBackground(new Color(255, 94, 58));
                setForeground(Color.WHITE);
            } else {
                setEnabled(false);
                setBackground(Color.LIGHT_GRAY);
            }

            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        private JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {

            Pedido p = listaPedidos.get(table.convertRowIndexToModel(row));

            button.setText("Cancelar");

            if (p.getEstadoActual().equals("PENDIENTE")) {
                button.setEnabled(true);
                button.setBackground(new Color(255, 94, 58));
                button.setForeground(Color.WHITE);
            } else {
                button.setEnabled(false);
                button.setBackground(Color.LIGHT_GRAY);
            }

            return button;
        }

        public Object getCellEditorValue() {

            int fila = tabla.getSelectedRow();
            Pedido p = listaPedidos.get(tabla.convertRowIndexToModel(fila));

            if (p.getEstadoActual().equals("PENDIENTE")) {

                int op = JOptionPane.showConfirmDialog(
                        null,
                        "¿Deseas cancelar el pedido con folio "
                                + p.getIdPedido() + "?",
                        "Confirmar cancelación",
                        JOptionPane.YES_NO_OPTION
                );

                if (op == JOptionPane.YES_OPTION) {
                    p.setEstadoActual("CANCELADO");
                    aplicarFiltros();
                }
            }

            return "Cancelar";
        }
    }
}