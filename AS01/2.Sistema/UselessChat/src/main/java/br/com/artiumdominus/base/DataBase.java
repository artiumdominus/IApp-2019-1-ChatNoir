package br.com.artiumdominus.base;

import java.sql.Connection;
import java.sql.Statement;
import java.io.FileReader;
import java.io.BufferedReader;

public class DataBase {

    private static Connection connection;
    private static Statement statement;

    public static void createDataBase() throws Exception {
        connection = ConexaoMySQL.getConexaoMySQL();
        if (connection != null) {
            statement = connection.createStatement();

            System.out.println("Criando Banco de Dados");

            String sql = "DROP DATABASE IF EXISTS Loneliness;";

            /*
            FileReader fReader = new FileReader("src/main/resources/Loneliness.sql");
            BufferedReader bReader = new BufferedReader(fReader);

            while (bReader.ready()) {
                sql += bReader.readLine() + "\n";
            }

            bReader.close();
            */

            statement.execute(sql);

            sql = "CREATE DATABASE IF NOT EXISTS Loneliness";

            statement.execute(sql);

            sql = "USE Loneliness;";

            statement.execute(sql);

            sql = "SET time_zone = \'+3:00\';";

            statement.execute(sql);

            sql = "CREATE TABLE Perfil (" +
                    "nome VARCHAR(70) NOT NULL," +
                    "username VARCHAR(70) NOT NULL," +
                    "numeroDeTelefone VARCHAR(70) NOT NULL," +
                    "bio VARCHAR(70)," +
                    "chatAddress VARCHAR(70) NOT NULL," +
                    "PRIMARY KEY (username)" +
            ");";

            statement.execute(sql);

            sql = "CREATE TABLE Grupo (" +
                    "nome VARCHAR(70) NOT NULL," +
                    "groupname VARCHAR(70) NOT NULL," +
                    "descricao VARCHAR(70)," +
                    "tipo TINYINT," +
                    "chatAddress VARCHAR(70) NULL," +
                    "PRIMARY KEY (groupname)" +
            ");";

            statement.execute(sql);

            sql = "CREATE TABLE Mensagem (" +
                    "id INT NOT NULL AUTO_INCREMENT," +
                    "conteudo VARCHAR(1000) NOT NULL," +
                    "envio DATETIME NOT NULL," +
                    "status VARCHAR(10)," +
                    "emissor VARCHAR(70)," +
                    "receptor VARCHAR(70)," +
                    "PRIMARY KEY (id)" +
            ");";

            statement.execute(sql);

            sql = "CREATE TABLE Administrador (" +
                    "groupname VARCHAR(70) NOT NULL," +
                    "username VARCHAR(70) NOT NULL" +
            ");";

            statement.execute(sql);

            statement.close();
            connection.close();
        }
    }

}
