package presentacion;

import Negocio.BOs.IPizzaBO;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.excepciones.PersistenciaException;

public class PantallaCarrito extends JFrame {

    private final IPizzaBO pizzaBO;

    private JPanel panelDetalleResumen;
    private JLabel lblDescuento;

    private JPanel panelItems;
    private JLabel lblTotal;
    private JTextField txtCupon;

    private List<ItemCarrito> carrito = new ArrayList<>();
    private double total = 0;
    private double descuento = 0;

    public PantallaCarrito(IPizzaBO pizzaBO) {
        this.pizzaBO = pizzaBO;

        setTitle("Papizza - Carrito");
        setSize(1150, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        crearHeader();
        crearContenido();
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
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblLogo.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 0));

        barraSuperior.add(lblLogo, BorderLayout.WEST);

        JPanel barraInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 12));
        barraInferior.setBackground(new Color(227, 101, 57));

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
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);

        btn.setBackground(new Color(227, 101, 57));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25));

        btn.addActionListener(e -> {

            switch (texto) {

                case "Inicio":
                {
                    try {
                        new PantallaInicioPedidoProgramado(pizzaBO).setVisible(true);
                    } catch (PersistenciaException ex) {
                        Logger.getLogger(PantallaCarrito.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    dispose();
                    break;


                case "Carrito":
                    break;

                case "Mis pedidos":
                    new PantallaMisPedidos(pizzaBO).setVisible(true);
                    dispose();
                    break;

                case "Actualizar":
                    JOptionPane.showMessageDialog(this,
                            "Función actualizar aún no implementada.");
                    break;
            }
        });

        return btn;
    }

    // Contenido
    private void crearContenido() {

        JPanel principal = new JPanel(new BorderLayout(40, 0));
        principal.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        principal.setBackground(new Color(230, 222, 220));

        panelItems = new JPanel();
        panelItems.setLayout(new BoxLayout(panelItems, BoxLayout.Y_AXIS));
        panelItems.setBackground(new Color(230, 222, 220));

        JScrollPane scroll = new JScrollPane(panelItems);
        scroll.setBorder(null);

        JPanel panelResumen = crearPanelResumen();

        principal.add(scroll, BorderLayout.CENTER);
        principal.add(panelResumen, BorderLayout.EAST);

        add(principal, BorderLayout.CENTER);
    }

    // Item con cant
    private void agregarItem(String nombre, double precio) {

        ItemCarrito item = new ItemCarrito(nombre, precio);
        carrito.add(item);
        total += precio;

        JPanel card = new JPanel(new BorderLayout());
        card.setMaximumSize(new Dimension(750, 120));
        card.setBackground(new Color(210, 200, 198));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Nombre y precio
        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setBackground(new Color(210, 200, 198));

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel lblPrecio = new JLabel("$" + precio + " MXN");
        lblPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        info.add(lblNombre);
        info.add(lblPrecio);

        // Cantidad
        JPanel panelCantidad = new JPanel(new FlowLayout());
        panelCantidad.setBackground(new Color(210, 200, 198));

        JButton btnMenos = new JButton("-");
        estiloBotonRedondo(btnMenos);
        btnMenos.setBackground(Color.WHITE);
        btnMenos.setForeground(Color.BLACK);
        JButton btnMas = new JButton("+");
        estiloBotonRedondo(btnMas);
        btnMas.setBackground(Color.WHITE);
        btnMas.setForeground(Color.BLACK);
        JLabel lblCantidad = new JLabel("1");

        btnMenos.addActionListener(e -> {
            if (item.cantidad > 1) {
                item.cantidad--;
                total -= precio;
                lblCantidad.setText(String.valueOf(item.cantidad));
                actualizarTotal();
            }
        });

        btnMas.addActionListener(e -> {
            item.cantidad++;
            total += precio;
            lblCantidad.setText(String.valueOf(item.cantidad));
            actualizarTotal();
        });

        panelCantidad.add(btnMenos);
        panelCantidad.add(lblCantidad);
        panelCantidad.add(btnMas);

        // Eliminar
        JButton btnEliminar = new JButton("Eliminar");
        estiloBotonRedondo(btnEliminar);
        btnEliminar.setBackground(new Color(196, 65, 65));
        btnEliminar.setForeground(Color.WHITE);

        btnEliminar.addActionListener(e -> {
            total -= (precio * item.cantidad);
            carrito.remove(item);
            panelItems.remove(card);
            actualizarTotal();
            panelItems.revalidate();
            panelItems.repaint();
        });

        JPanel derecha = new JPanel(new BorderLayout());
        derecha.setBackground(new Color(210, 200, 198));
        derecha.add(panelCantidad, BorderLayout.NORTH);
        derecha.add(btnEliminar, BorderLayout.SOUTH);

        card.add(info, BorderLayout.CENTER);
        card.add(derecha, BorderLayout.EAST);

        panelItems.add(card);
        panelItems.add(Box.createVerticalStrut(20));

        actualizarTotal();
    }

    // Resumen de los datos del pedido, incluye lo del cupón
    private JPanel crearPanelResumen() {

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(380, 500));
        panel.setBackground(new Color(220, 215, 214));
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel lblTitulo = new JLabel("Resumen del pedido");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        panelDetalleResumen = new JPanel();
        panelDetalleResumen.setLayout(new BoxLayout(panelDetalleResumen, BoxLayout.Y_AXIS));
        panelDetalleResumen.setBackground(new Color(220, 215, 214));

        JScrollPane scrollResumen = new JScrollPane(panelDetalleResumen);
        scrollResumen.setBorder(null);
        scrollResumen.setBackground(new Color(220, 215, 214));

        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));
        panelInferior.setBackground(new Color(220, 215, 214));

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scrollResumen, BorderLayout.CENTER);
        panel.add(panelInferior, BorderLayout.SOUTH);

        return panel;
    }

    private void aplicarCupon() {

        String codigo = txtCupon.getText().trim();

        if (codigo.equalsIgnoreCase("PAPI10")) {
            descuento = total * 0.10;
            JOptionPane.showMessageDialog(this, "Cupón aplicado 10%");
        } else {
            descuento = 0;
            JOptionPane.showMessageDialog(this, "Cupón inválido");
        }

        actualizarTotal();
    }

    private void actualizarTotal() {

        panelDetalleResumen.removeAll();

        // LISTA DE PRODUCTOS 
        for (ItemCarrito item : carrito) {

            JPanel fila = new JPanel(new BorderLayout());
            fila.setBackground(new Color(220, 215, 214));

            JLabel lblNombre = new JLabel(
                    "x" + item.cantidad + " " + item.nombre);
            lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            double subtotal = item.precio * item.cantidad;

            JLabel lblPrecio = new JLabel(
                    "+ $" + String.format("%.2f", subtotal) + " MXN");
            lblPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            fila.add(lblNombre, BorderLayout.WEST);
            fila.add(lblPrecio, BorderLayout.EAST);

            panelDetalleResumen.add(fila);

            JPanel filaNota = new JPanel(new BorderLayout(10, 0));
            filaNota.setBackground(new Color(220, 215, 214));

            JLabel lblNota = new JLabel("Nota");
            lblNota.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            JTextField txtNota = new JTextField();
            txtNota.setBackground(new Color(200, 200, 200));
            txtNota.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));

            filaNota.add(lblNota, BorderLayout.WEST);
            filaNota.add(txtNota, BorderLayout.CENTER);

            panelDetalleResumen.add(filaNota);
            panelDetalleResumen.add(Box.createVerticalStrut(15));
        }

        panelDetalleResumen.add(Box.createVerticalStrut(10));

        // LÍNEA DESCUENTO COMO TU IMAGEN 
        JPanel filaDescuento = new JPanel(new BorderLayout());
        filaDescuento.setBackground(new Color(220, 215, 214));

        JLabel lblTextoCupon = new JLabel("Código promocional");
        lblTextoCupon.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblMontoDescuento = new JLabel(
                "- $" + String.format("%.2f", descuento) + " MXN");
        lblMontoDescuento.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        filaDescuento.add(lblTextoCupon, BorderLayout.WEST);
        filaDescuento.add(lblMontoDescuento, BorderLayout.EAST);

        panelDetalleResumen.add(filaDescuento);
        panelDetalleResumen.add(Box.createVerticalStrut(10));

        // INPUT CUPÓN + VALIDAR ABAJO
        JPanel filaCuponInput = new JPanel(new BorderLayout(10, 0));
        filaCuponInput.setBackground(new Color(220, 215, 214));

        txtCupon = new JTextField();
        txtCupon.setBackground(new Color(200, 200, 200));
        txtCupon.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));

        JButton btnValidar = new JButton("Validar");
        estiloBotonRedondo(btnValidar);

        btnValidar.addActionListener(e -> aplicarCupon());

        filaCuponInput.add(txtCupon, BorderLayout.CENTER);
        filaCuponInput.add(btnValidar, BorderLayout.EAST);

        panelDetalleResumen.add(filaCuponInput);
        panelDetalleResumen.add(Box.createVerticalStrut(20));

        // TOTAL 
        double totalFinal = total - descuento;

        // FILA TOTAL ESTILO FACTURA 
        JPanel filaTotal = new JPanel(new BorderLayout());
        filaTotal.setBackground(new Color(220, 215, 214));

        JLabel lblTextoTotal = new JLabel("Total");
        lblTextoTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblMontoTotal = new JLabel(
                "$ " + String.format("%.2f", totalFinal) + " MXN"
        );
        lblMontoTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));

        filaTotal.add(lblTextoTotal, BorderLayout.WEST);
        filaTotal.add(lblMontoTotal, BorderLayout.EAST);

        panelDetalleResumen.add(Box.createVerticalStrut(10));
        panelDetalleResumen.add(filaTotal);
        panelDetalleResumen.add(Box.createVerticalStrut(20));

        JButton btnOrdenar = new JButton("Ordenar");
        estiloBotonRedondo(btnOrdenar);

        btnOrdenar.addActionListener(e
                -> JOptionPane.showMessageDialog(this,
                        "Pedido enviado correctamente")
        );

        panelDetalleResumen.add(btnOrdenar);
        btnOrdenar.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelDetalleResumen.revalidate();
        panelDetalleResumen.repaint();
    }

    private void estiloBotonRedondo(JButton btn) {

        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(255, 94, 58));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(btn.getBackground());
                g2.fillRoundRect(0, 0, btn.getWidth(),
                        btn.getHeight(), 30, 30);

                super.paint(g2, c);
                g2.dispose();
            }
        });
    }

    // MODELO 
    private static class ItemCarrito {

        String nombre;
        double precio;
        int cantidad = 1;

        public ItemCarrito(String nombre, double precio) {
            this.nombre = nombre;
            this.precio = precio;
        }
    }

}
