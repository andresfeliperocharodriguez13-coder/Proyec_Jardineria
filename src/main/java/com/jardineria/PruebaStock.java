package com.jardineria;

import com.jardineria.domain.model.DetallePedido;
import com.jardineria.domain.model.repository.DetallePedidoRepository;
import com.jardineria.infrastructure.database.persistence.DetallePedidoRepositoryMySQL;
import com.jardineria.application.usecase.detallepedido.GuardarDetallePedidoUseCase;

import java.math.BigDecimal;

public class PruebaStock {

    public static void main(String[] args) {

        DetallePedidoRepository repository =
                new DetallePedidoRepositoryMySQL();

        GuardarDetallePedidoUseCase guardar =
                new GuardarDetallePedidoUseCase(repository);

        DetallePedido detalle = new DetallePedido(
                1,
                "TEST001",
                3,
                new BigDecimal("15000"),
                1
        );

        try {
            guardar.ejecutar(detalle);

            System.out.println("¡Detalle guardado correctamente!");
            System.out.println("Producto: TEST001");
            System.out.println("Cantidad: 3");
            System.out.println("Pedido: 1");

        } catch (Exception e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }
}