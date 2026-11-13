
package com.jardineria.presentation;

import com.jardineria.application.usecase.producto.*;
import com.jardineria.domain.model.Producto;
import com.jardineria.infrastructure.database.persistence.ProductoRepositoryMySQL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class ProductosPanel extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;

    private final ProductoRepositoryMySQL repository =
            new ProductoRepositoryMySQL();

    public ProductosPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 248, 245));

        JLabel titulo = new JLabel("Gestión de Productos");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        modelo = new DefaultTableModel(
                new String[]{"Código", "Nombre", "Gama", "Proveedor",
                        "Stock", "Precio venta"}, 0) {
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

        registrar.addActionListener(e -> registrarProducto());
        editar.addActionListener(e -> editarProducto());
        eliminar.addActionListener(e -> eliminarProducto());
        actualizar.addActionListener(e -> cargarProductos());

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

        cargarProductos();
    }

    private void cargarProductos() {
        System.out.println("ENTRÉ A CARGAR PRODUCTOS");
        modelo.setRowCount(0);

        try {
            ListarProductosUseCase listar =
                    new ListarProductosUseCase(repository);

            List<Producto> productos = listar.ejecutar();
            System.out.println("Productos encontrados: " + productos.size());

            for (Producto producto : productos) {
                modelo.addRow(new Object[]{
                        producto.getCodigoProducto(),
                        producto.getNombre(),
                        producto.getGama(),
                        producto.getProveedor(),
                        producto.getCantidadEnStock(),
                        producto.getPrecioVenta()
                });
            }
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void registrarProducto() {
        Producto producto = mostrarFormulario(null);
        if (producto == null) return;

        try {
            new GuardarProductoUseCase(repository).ejecutar(producto);
            JOptionPane.showMessageDialog(this,
                    "Producto registrado correctamente.");
            cargarProductos();
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void editarProducto() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un producto primero.");
            return;
        }

        String codigo = modelo.getValueAt(fila, 0).toString();

        Producto producto = repository.buscarPorId(codigo).orElse(null);

        if (producto == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró el producto.");
            return;
        }

        Producto actualizado = mostrarFormulario(producto);
        if (actualizado == null) return;

        try {
            new ActualizarProductoUseCase(repository).ejecutar(actualizado);
            JOptionPane.showMessageDialog(this,
                    "Producto actualizado correctamente.");
            cargarProductos();
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void eliminarProducto() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un producto primero.");
            return;
        }

        String codigo = modelo.getValueAt(fila, 0).toString();

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar este producto?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) return;

        try {
            new EliminarProductoUseCase(repository).ejecutar(codigo);
            JOptionPane.showMessageDialog(this,
                    "Producto eliminado correctamente.");
            cargarProductos();
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private Producto mostrarFormulario(Producto producto) {
        String[] nombres = {
                "Código", "Nombre", "Gama", "Dimensiones",
                "Proveedor", "Descripción", "Cantidad en stock",
                "Precio venta", "Precio proveedor"
        };

        JTextField[] campos = new JTextField[nombres.length];
        JPanel formulario = new JPanel(new GridLayout(nombres.length, 2, 5, 5));

        for (int i = 0; i < nombres.length; i++) {
            formulario.add(new JLabel(nombres[i]));
            campos[i] = new JTextField();
            formulario.add(campos[i]);
        }

        if (producto != null) {
            campos[0].setText(producto.getCodigoProducto());
            campos[1].setText(producto.getNombre());
            campos[2].setText(producto.getGama());
            campos[3].setText(producto.getDimensiones());
            campos[4].setText(producto.getProveedor());
            campos[5].setText(producto.getDescripcion());
            campos[6].setText(String.valueOf(producto.getCantidadEnStock()));
            campos[7].setText(producto.getPrecioVenta().toString());
            campos[8].setText(producto.getPrecioProveedor().toString());
            campos[0].setEditable(false);
        }

        int resultado = JOptionPane.showConfirmDialog(
                this,
                new JScrollPane(formulario),
                producto == null ? "Registrar producto" : "Editar producto",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) return null;

        try {
            for (int i = 0; i < campos.length; i++) {
                if (campos[i].getText().trim().isEmpty()) {
                    throw new IllegalArgumentException(
                            "Todos los campos son obligatorios.");
                }
            }

            return new Producto(
                    campos[0].getText().trim(),
                    campos[1].getText().trim(),
                    campos[2].getText().trim(),
                    campos[3].getText().trim(),
                    campos[4].getText().trim(),
                    campos[5].getText().trim(),
                    Integer.parseInt(campos[6].getText().trim()),
                    new BigDecimal(campos[7].getText().trim()),
                    new BigDecimal(campos[8].getText().trim())
            );

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Stock y precios deben tener valores numéricos válidos.");
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
