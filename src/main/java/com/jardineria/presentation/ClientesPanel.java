
package com.jardineria.presentation;

import com.jardineria.application.usecase.cliente.*;
import com.jardineria.domain.model.Cliente;
import com.jardineria.infrastructure.database.persistence.ClienteRepositoryMySQL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class ClientesPanel extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;

    private final ClienteRepositoryMySQL repository =
            new ClienteRepositoryMySQL();

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

        JButton registrar = new JButton("Registrar");
        JButton editar = new JButton("Editar");
        JButton eliminar = new JButton("Eliminar");
        JButton actualizar = new JButton("Actualizar");

        registrar.addActionListener(e -> registrarCliente());
        editar.addActionListener(e -> editarCliente());
        eliminar.addActionListener(e -> eliminarCliente());
        actualizar.addActionListener(e -> cargarClientes());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.setBackground(new Color(245, 248, 245));

        botones.add(registrar);
        botones.add(editar);
        botones.add(eliminar);
        botones.add(actualizar);

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBackground(new Color(245, 248, 245));
        superior.add(titulo, BorderLayout.NORTH);
        superior.add(botones, BorderLayout.SOUTH);

        add(superior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarClientes();
    }

    private void cargarClientes() {

        modelo.setRowCount(0);

        try {
            ListarClienteUseCase listar =
                    new ListarClienteUseCase(repository);

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
            mostrarError(e);
        }
    }

    private void registrarCliente() {

        Cliente cliente = mostrarFormulario(null);

        if (cliente == null) {
            return;
        }

        try {
            new GuardarClienteUseCase(repository).ejecutar(cliente);

            JOptionPane.showMessageDialog(this,
                    "Cliente registrado correctamente.");

            cargarClientes();

        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void editarCliente() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un cliente primero.");
            return;
        }

        int codigo = (int) modelo.getValueAt(fila, 0);

        Cliente cliente = repository.buscarPorId(codigo).orElse(null);

        if (cliente == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró el cliente.");
            return;
        }

        Cliente actualizado = mostrarFormulario(cliente);

        if (actualizado == null) {
            return;
        }

        try {
            new ActualizarClienteUseCase(repository).ejecutar(actualizado);

            JOptionPane.showMessageDialog(this,
                    "Cliente actualizado correctamente.");

            cargarClientes();

        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void eliminarCliente() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un cliente primero.");
            return;
        }

        int codigo = (int) modelo.getValueAt(fila, 0);

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar este cliente?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            new EliminarClienteUseCase(repository).ejecutar(codigo);

            JOptionPane.showMessageDialog(this,
                    "Cliente eliminado correctamente.");

            cargarClientes();

        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private Cliente mostrarFormulario(Cliente cliente) {

        JTextField[] campos = new JTextField[14];

        String[] nombres = {
                "Código cliente", "Nombre cliente",
                "Nombre contacto", "Apellido contacto",
                "Teléfono", "Fax", "Dirección 1",
                "Dirección 2", "Ciudad", "Región",
                "País", "Código postal",
                "Código empleado ventas", "Límite de crédito"
        };

        JPanel formulario = new JPanel(new GridLayout(14, 2, 5, 5));

        for (int i = 0; i < nombres.length; i++) {
            formulario.add(new JLabel(nombres[i]));
            campos[i] = new JTextField();
            formulario.add(campos[i]);
        }

        if (cliente != null) {
            campos[0].setText(String.valueOf(cliente.getCodigoCliente()));
            campos[1].setText(cliente.getNombreCliente());
            campos[2].setText(cliente.getNombreContacto());
            campos[3].setText(cliente.getApellidoContacto());
            campos[4].setText(cliente.getTelefono());
            campos[5].setText(cliente.getFax());
            campos[6].setText(cliente.getLineaDireccion1());
            campos[7].setText(cliente.getLineaDireccion2());
            campos[8].setText(cliente.getCiudad());
            campos[9].setText(cliente.getRegion());
            campos[10].setText(cliente.getPais());
            campos[11].setText(cliente.getCodigoPostal());

            campos[12].setText(
                    cliente.getCodigoEmpleadoRepVentas() == null
                            ? "" : cliente.getCodigoEmpleadoRepVentas().toString()
            );

            campos[13].setText(
                    cliente.getLimiteCredito() == null
                            ? "" : cliente.getLimiteCredito().toString()
            );

            campos[0].setEditable(false);
        }

        int resultado = JOptionPane.showConfirmDialog(
                this,
                new JScrollPane(formulario),
                cliente == null ? "Registrar cliente" : "Editar cliente",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            int codigo = Integer.parseInt(campos[0].getText().trim());

            if (campos[1].getText().trim().isEmpty()
                    || campos[4].getText().trim().isEmpty()
                    || campos[6].getText().trim().isEmpty()
                    || campos[8].getText().trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Completa nombre, teléfono, dirección y ciudad."
                );
            }

            String nombreContacto = opcional(campos[2].getText());
            String apellidoContacto = opcional(campos[3].getText());

            Integer empleado = campos[12].getText().trim().isEmpty()
                    ? null : Integer.parseInt(campos[12].getText().trim());

            BigDecimal credito = campos[13].getText().trim().isEmpty()
                    ? null : new BigDecimal(campos[13].getText().trim());

            return new Cliente(
                    codigo,
                    campos[1].getText().trim(),
                    nombreContacto,
                    apellidoContacto,
                    campos[4].getText().trim(),
                    campos[5].getText().trim(),
                    campos[6].getText().trim(),
                    opcional(campos[7].getText()),
                    campos[8].getText().trim(),
                    opcional(campos[9].getText()),
                    opcional(campos[10].getText()),
                    opcional(campos[11].getText()),
                    empleado,
                    credito
            );

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El código, empleado y crédito deben tener valores válidos.");
            return null;

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return null;
        }
    }

    private String opcional(String valor) {
        return valor.trim().isEmpty() ? null : valor.trim();
    }

    private void mostrarError(Exception e) {
        JOptionPane.showMessageDialog(
                this,
                "Ocurrió un error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
