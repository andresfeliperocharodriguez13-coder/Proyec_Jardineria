
package com.jardineria.presentation;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel contenido;
    private CardLayout tarjetas;

    public MainFrame() {

        setTitle("Sistema de Jardinería");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel principal = new JPanel(new BorderLayout());

        // Menu lateral
        JPanel menu = new JPanel();
        menu.setBackground(new Color(46, 125, 50));
        menu.setPreferredSize(new Dimension(190, 600));
        menu.setLayout(new GridLayout(7, 1, 5, 5));

        JLabel titulo = new JLabel("JARDINERÍA", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        menu.add(titulo);

        // Paneles de contenido
        tarjetas = new CardLayout();
        contenido = new JPanel(tarjetas);

        contenido.add(crearPanel("Bienvenido al Sistema de Jardinería"), "Inicio");
        contenido.add(new ClientesPanel(), "Clientes");
        contenido.add(new EmpleadosPanel(), "Empleados");
        contenido.add(new ProductosPanel(), "Productos");
        contenido.add(crearPanel("Módulo de Pedidos"), "Pedidos");
        contenido.add(crearPanel("Módulo de Pagos"), "Pagos");

        // Botones
        String[] opciones = {
                "Inicio",
                "Clientes",
                "Empleados",
                "Productos",
                "Pedidos",
                "Pagos"
        };

        for (String opcion : opciones) {

            JButton boton = new JButton(opcion);
            boton.setFocusPainted(false);
            boton.setBackground(Color.WHITE);
            boton.setForeground(new Color(46, 125, 50));

            boton.addActionListener(e -> {
                tarjetas.show(contenido, opcion);
            });

            menu.add(boton);
        }

        principal.add(menu, BorderLayout.WEST);
        principal.add(contenido, BorderLayout.CENTER);

        add(principal);
    }

    private JPanel crearPanel(String texto) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 245));

        JLabel titulo = new JLabel(texto, SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 25));
        titulo.setForeground(new Color(46, 125, 50));

        panel.add(titulo, BorderLayout.CENTER);

        return panel;
    }
}
