package presentacion;

import javax.swing.*;
import java.awt.*;

public class PantallaDetallePizza extends JFrame {

    public PantallaDetallePizza(String nombre, double precioBase) {

        setTitle("Detalle - " + nombre);
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        crearContenido(nombre, precioBase);
    }

    private void crearContenido(String nombre, double precioBase) {

        JPanel panelPrincipal = new JPanel(new BorderLayout(40, 20));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelPrincipal.setBackground(new Color(235, 228, 226));

        // Img izquierda
        JLabel lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(350, 300));
        lblImagen.setOpaque(true);
        lblImagen.setBackground(Color.GRAY); // luego aquí puedes poner imagen real
        panelPrincipal.add(lblImagen, BorderLayout.WEST);

        // Info derecha
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(new Color(235, 228, 226));

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 34));

        JLabel lblDescripcion = new JLabel("<html>\"Nuestra interpretación de un clásico legendario..."
                + "<br>Base crujiente con queso y pepperoni premium.\"</html>");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        panelInfo.add(lblNombre);
        panelInfo.add(Box.createVerticalStrut(20));
        panelInfo.add(lblDescripcion);
        panelInfo.add(Box.createVerticalStrut(30));

        // Tamaños de la pizza predeterminados
        JRadioButton rbChica = new JRadioButton("Chica (4 rebanadas): $" + precioBase);
        JRadioButton rbMediana = new JRadioButton("Mediana (6 rebanadas): $" + precioBase + 40);
        JRadioButton rbGrande = new JRadioButton("Grande (8 rebanadas): $" + (precioBase + 60));

        rbChica.setBackground(new Color(235, 228, 226));
        rbMediana.setBackground(new Color(235, 228, 226));
        rbGrande.setBackground(new Color(235, 228, 226));

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbChica);
        grupo.add(rbMediana);
        grupo.add(rbGrande);

        rbChica.setSelected(true);

        panelInfo.add(rbChica);
        panelInfo.add(rbMediana);
        panelInfo.add(rbGrande);
        panelInfo.add(Box.createVerticalStrut(30));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        panelBotones.setBackground(new Color(235, 228, 226));

        JButton btnVolver = new JButton("Volver");
        estiloBotonRedondo(btnVolver);
        btnVolver.setBackground(new Color(200, 200, 200));

        JButton btnAgregar = new JButton("+ Añadir");
        estiloBotonRedondo(btnAgregar);
        btnAgregar.setBackground(new Color(255, 94, 58));
        btnAgregar.setForeground(Color.WHITE);

        btnVolver.addActionListener(e -> dispose());

        btnAgregar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Pizza agregada al carrito");
            dispose();
        });

        panelBotones.add(btnVolver);
        panelBotones.add(btnAgregar);

        panelInfo.add(Box.createVerticalGlue());
        panelInfo.add(panelBotones);

        panelPrincipal.add(panelInfo, BorderLayout.CENTER);

        add(panelPrincipal, BorderLayout.CENTER);
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
}
