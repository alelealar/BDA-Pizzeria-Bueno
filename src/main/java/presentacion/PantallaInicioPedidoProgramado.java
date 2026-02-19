package presentacion;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PantallaInicioPedidoProgramado extends JFrame {

    private JPanel panelCatalogo;
    private JLabel lblTotal;
    private List<PizzaItem> carrito = new ArrayList<>();
    private double total = 0;

    private final Font FONT_LOGO = new Font("Segoe UI", Font.BOLD, 34);
    private final Font FONT_MENU = new Font("Segoe UI", Font.PLAIN, 16);
    private final Font FONT_TITULO_PIZZA = new Font("Segoe UI", Font.BOLD, 20);
    private final Font FONT_PRECIO = new Font("Segoe UI", Font.PLAIN, 18);
    private final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 16);
    private final Font FONT_TOTAL = new Font("Segoe UI", Font.BOLD, 20);

    public PantallaInicioPedidoProgramado() {
        setTitle("Papizza - Pedido Programado");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        crearHeader();
        crearCatalogo();
        //crearFooter();
    }

    // HEADER 
    private void crearHeader() {

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        // Barra superior
        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(new Color(138, 37, 22));
        barraSuperior.setPreferredSize(new Dimension(1000, 80));

        JLabel lblLogo = new JLabel("PAPIZZA");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(FONT_LOGO);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 0));

        barraSuperior.add(lblLogo, BorderLayout.WEST);

        // Barra inferior
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
                    new PantallaCarrito().setVisible(true);
                    dispose(); // cierra esta pantalla
                    break;

                case "Inicio":
                    break;

                case "Actualizar":
                    JOptionPane.showMessageDialog(this,
                            "Función actualizar aún no implementada.");
                    break;

                case "Mis pedidos":
                    new PantallaMisPedidos().setVisible(true);
                    dispose();
                    break;
            }
        });

        return btn;
    }

    // Cátalogo
    private void crearCatalogo() {

        panelCatalogo = new JPanel();
        panelCatalogo.setLayout(new GridLayout(0, 3, 25, 25));
        panelCatalogo.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panelCatalogo.setBackground(new Color(245, 245, 245));

        agregarPizza("Pepperoni Supreme", 180);
        agregarPizza("Mar y Tierra", 210);
        agregarPizza("Clásica Americana", 170);
        agregarPizza("Bosque Blanco", 190);
        agregarPizza("Veggie Deluxe", 160);
        agregarPizza("Hawaiian Punch", 175);

        JScrollPane scroll = new JScrollPane(panelCatalogo);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);
    }

    private void agregarPizza(String nombre, double precio) {

        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setBackground(Color.WHITE);

        JLabel lblNombre = new JLabel(nombre, SwingConstants.CENTER);
        lblNombre.setFont(FONT_TITULO_PIZZA);

        JLabel lblPrecio = new JLabel("$" + precio, SwingConstants.CENTER);
        lblPrecio.setFont(FONT_PRECIO);
        lblPrecio.setForeground(new Color(100, 100, 100));

        JButton btnAgregar = new JButton("Añadir");
        estiloBotonRedondo(btnAgregar);
        btnAgregar.setFont(FONT_BOTON);
        btnAgregar.setBackground(new Color(255, 94, 58));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);

        btnAgregar.addActionListener(e -> {
            PantallaDetallePizza detalle = new PantallaDetallePizza(nombre, precio);
            detalle.setVisible(true);
        });

        card.add(lblNombre, BorderLayout.NORTH);
        card.add(lblPrecio, BorderLayout.CENTER);
        card.add(btnAgregar, BorderLayout.SOUTH);

        panelCatalogo.add(card);
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

    // CARRITO 
    private void agregarAlCarrito(String nombre, double precio) {
        carrito.add(new PizzaItem(nombre, precio));
        total += precio;
        lblTotal.setText("Total: $" + String.format("%.2f", total));
    }

    // FOOTER 
    private void crearFooter() {

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        footer.setPreferredSize(new Dimension(1000, 90));
        footer.setBackground(Color.WHITE);

        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(FONT_TOTAL);

        JButton btnFinalizar = new JButton("Finalizar Pedido");
        btnFinalizar.setFont(FONT_BOTON);
        btnFinalizar.setBackground(new Color(50, 150, 50));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFocusPainted(false);

        btnFinalizar.addActionListener(e -> finalizarPedido());

        footer.add(lblTotal);
        footer.add(btnFinalizar);

        add(footer, BorderLayout.SOUTH);
    }

    private void finalizarPedido() {

        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe agregar al menos una pizza.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Pedido listo para registrarse.\nTotal: $" + total,
                "Confirmación",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()
                -> new PantallaInicioPedidoProgramado().setVisible(true)
        );
    }

    private static class PizzaItem {

        String nombre;
        double precio;

        public PizzaItem(String nombre, double precio) {
            this.nombre = nombre;
            this.precio = precio;
        }
    }
}