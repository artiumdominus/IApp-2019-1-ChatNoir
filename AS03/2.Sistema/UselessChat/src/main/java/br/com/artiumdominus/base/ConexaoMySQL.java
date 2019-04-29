package br.com.artiumdominus.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMySQL {

    public static String status = "Não conectou...";

    public ConexaoMySQL() {}

    public static Connection getConexaoMySQL() {
        Connection connection = null;

        try {

            String driverName = "com.mysql.cj.jdbc.Driver";
            Class.forName(driverName);

            String serverName = "localhost";
            String mydatabase = "mysql";
            String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
            String username = "artiumdominus";
            String password = "";
            connection = DriverManager.getConnection(url, username, password);

            if (connection != null) {
                status = ("STATUS--->Conectado com sucesso!");
            } else {
                status = ("STATUS--->Não foi possível realizar conexão");
            }

            return connection;

        } catch (ClassNotFoundException e) {
            System.out.println("O driver especificado não foi encontrado.");
            return null;
        } catch (SQLException e) {
            System.out.println("Não foi possível conectar ao Banco de Dados.");
            return null;
        }
    }

    public static String statusConnection() {
        return status;
    }

    public static boolean fecharConexao() {
        try {
            getConexaoMySQL().close();
            status = "STATUS--->Conexão encerrada.";
            return true;
        } catch (SQLException e) {
            status = "STATUS--->Falha no encerramento da conexão.";
            return false;
        }
    }

    public static Connection ReiniciarConexao() {
        fecharConexao();
        return getConexaoMySQL();
    }

}
