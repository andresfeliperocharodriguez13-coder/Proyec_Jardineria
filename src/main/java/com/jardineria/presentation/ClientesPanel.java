
package com.jardineria.presentation;

import com.jardineria.application.usecase.cliente.ListarClienteUseCase;
import com.jardineria.domain.model.Cliente;
import com.jardineria.infrastructure.database.persistence.ClienteRepositoryMySQL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientesPanel extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;

    public ClientesPanel() {

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 248, 245));

        JLabel titulo = new JLabel("Gestión de Clientes");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        modelo = new DefaultTableModel(
                new String[]{"Código", "Cliente", "Contacto",
                        "Teléfono", "Ciudad", "País"}, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);

        JButton actualizar = new JButton("Actualizar tabla");
        actualizar.addActionListener(e -> cargarClientes());

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBackground(new Color(245, 248, 245));
        superior.add(titulo, BorderLayout.WEST);
        superior.add(actualizar, BorderLayout.EAST);

        add(superior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarClientes();
    }

    private void cargarClientes() {

        modelo.setRowCount(0);

        try {
            ListarClienteUseCase listar = new ListarClienteUseCase(
                    new ClienteRepositoryMySQL()
            );

            List<Cliente> clientes = listar.ejecutar();

            for (Cliente cliente : clientes) {
                modelo.addRow(new Object[]{
                        cliente.getCodigoCliente(),
                        cliente.getNombreCliente(),
                        cliente.getNombreContacto(),
                        cliente.getTelefono(),
                        cliente.getCiudad(),
                        cliente.getPais()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los clientes: " + e.getMessage()
            );
        }
    }
}
