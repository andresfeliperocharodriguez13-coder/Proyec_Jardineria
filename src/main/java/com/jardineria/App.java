package com.jardineria;

import com.jardineria.infrastructure.database.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;

public class App {

    public static void main(String[] args) {

        try (Connection connection = ConnectionFactory
                .getInstance()
                .getConnection()) {

            System.out.println("¡Conexión exitosa con MySQL!");
            System.out.println("Base de datos: "
                    + connection.getCatalog());

        } catch (SQLException e) {
            System.out.println("Error al conectar con MySQL:");
            e.printStackTrace();
        }
    }
}
