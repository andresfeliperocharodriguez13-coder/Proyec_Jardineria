
package com.jardineria.presentation;

import com.jardineria.application.usecase.empleado.*;
import com.jardineria.domain.model.Empleado;
import com.jardineria.infrastructure.database.persistence.EmpleadoRepositoryMySQL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmpleadosPanel extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;

    private final EmpleadoRepositoryMySQL repository =
            new EmpleadoRepositoryMySQL();

    public EmpleadosPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 248, 245));

        JLabel titulo = new JLabel("Gestión de Empleados");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        modelo = new DefaultTableModel(
                new String[]{"Código", "Nombre", "Apellido",
                        "Correo", "Oficina", "Puesto"}, 0) {
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

        registrar.addActionListener(e -> registrarEmpleado());
        editar.addActionListener(e -> editarEmpleado());
        eliminar.addActionListener(e -> eliminarEmpleado());
        actualizar.addActionListener(e -> cargarEmpleados());

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

        cargarEmpleados();
    }

    private void cargarEmpleados() {
        modelo.setRowCount(0);

        try {
            ListarEmpleadosUseCase listar =
                    new ListarEmpleadosUseCase(repository);

            List<Empleado> empleados = listar.ejecutar();

            for (Empleado empleado : empleados) {
                modelo.addRow(new Object[]{
                        empleado.getCodigoEmpleado(),
                        empleado.getNombre(),
                        empleado.getApellido1(),
                        empleado.getEmail(),
                        empleado.getCodigoOficina(),
                        empleado.getPuesto()
                });
            }
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void registrarEmpleado() {
        Empleado empleado = mostrarFormulario(null);
        if (empleado == null) return;

        try {
            new GuardarEmpleadoUseCase(repository).ejecutar(empleado);
            JOptionPane.showMessageDialog(this,
                    "Empleado registrado correctamente.");
            cargarEmpleados();
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void editarEmpleado() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un empleado primero.");
            return;
        }

        int codigo = Integer.parseInt(
                modelo.getValueAt(fila, 0).toString());

        Empleado empleado = repository.buscarPorId(codigo).orElse(null);

        if (empleado == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró el empleado.");
            return;
        }

        Empleado actualizado = mostrarFormulario(empleado);
        if (actualizado == null) return;

        try {
            new ActualizarEmpleadoUseCase(repository).ejecutar(actualizado);
            JOptionPane.showMessageDialog(this,
                    "Empleado actualizado correctamente.");
            cargarEmpleados();
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void eliminarEmpleado() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un empleado primero.");
            return;
        }

        int codigo = Integer.parseInt(
                modelo.getValueAt(fila, 0).toString());

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar este empleado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) return;

        try {
            new EliminarEmpleadoUseCase(repository).ejecutar(codigo);
            JOptionPane.showMessageDialog(this,
                    "Empleado eliminado correctamente.");
            cargarEmpleados();
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private Empleado mostrarFormulario(Empleado empleado) {
        String[] nombres = {
                "Código empleado", "Nombre", "Primer apellido",
                "Segundo apellido", "Extensión", "Correo",
                "Código oficina", "Código jefe", "Puesto"
        };

        JTextField[] campos = new JTextField[nombres.length];
        JPanel formulario = new JPanel(
                new GridLayout(nombres.length, 2, 5, 5));

        for (int i = 0; i < nombres.length; i++) {
            formulario.add(new JLabel(nombres[i]));
            campos[i] = new JTextField();
            formulario.add(campos[i]);
        }

        if (empleado != null) {
            campos[0].setText(String.valueOf(empleado.getCodigoEmpleado()));
            campos[1].setText(empleado.getNombre());
            campos[2].setText(empleado.getApellido1());
            campos[3].setText(empleado.getApellido2());
            campos[4].setText(empleado.getExtension());
            campos[5].setText(empleado.getEmail());
            campos[6].setText(empleado.getCodigoOficina());
            campos[7].setText(empleado.getCodigoJefe() == null
                    ? "" : empleado.getCodigoJefe().toString());
            campos[8].setText(empleado.getPuesto());
            campos[0].setEditable(false);
        }

        int resultado = JOptionPane.showConfirmDialog(
                this,
                new JScrollPane(formulario),
                empleado == null ? "Registrar empleado" : "Editar empleado",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) return null;

        try {
            for (int i : new int[]{0, 1, 2, 4, 5, 6, 8}) {
                if (campos[i].getText().trim().isEmpty()) {
                    throw new IllegalArgumentException(
                            "Completa los campos obligatorios.");
                }
            }

            Integer jefe = campos[7].getText().trim().isEmpty()
                    ? null : Integer.parseInt(campos[7].getText().trim());

            return new Empleado(
                    Integer.parseInt(campos[0].getText().trim()),
                    campos[1].getText().trim(),
                    campos[2].getText().trim(),
                    campos[3].getText().trim(),
                    campos[4].getText().trim(),
                    campos[5].getText().trim(),
                    campos[6].getText().trim(),
                    jefe,
                    campos[8].getText().trim()
            );

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El código del empleado y del jefe deben ser números.");
            return null;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return null;
        }
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
