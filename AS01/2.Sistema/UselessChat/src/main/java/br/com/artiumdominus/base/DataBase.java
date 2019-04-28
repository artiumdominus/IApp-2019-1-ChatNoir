package br.com.artiumdominus.base;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.sql.ResultSet;

import br.com.artiumdominus.Utils;

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

    public static void gerarArquivosDeImportacao() throws SQLException {
        connection = ConexaoMySQL.getConexaoMySQL();
        FileWriter fw;

        if (connection != null) {
            statement = connection.createStatement();

            System.out.println("Gerando Arquivos...");

            String sql = "USE UselessChat";
            ResultSet result;

            statement.execute(sql);

            // LOGON
            sql = "SELECT * FROM Perfil";

            result = statement.executeQuery(sql);
            try {
                fw = new FileWriter("src/main/resources/logon.txt");
                while (result.next()) {
                    fw.write("logon" +
                            Utils.ALFA(result.getString("username"), 70) +
                            Utils.ALFA(result.getString("senha"), 20) +
                            Utils.ALFA(result.getString("nome"), 70) +
                            Utils.ALFA(result.getString("numeroDeTelefone"), 70) +
                            Utils.ALFA(result.getString("bio"), 70) +
                            "\n"
                    );

                }
                fw.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            // LOGIN
            result = statement.executeQuery(sql);
            try {
                fw = new FileWriter("src/main/resources/login.txt");
                while (result.next()) {
                    fw.write("login" +
                            Utils.ALFA(result.getString("username"), 70) +
                            Utils.ALFA(result.getString("senha"), 20) +
                            "\n"
                    );
                }
                fw.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            // LOGOUT
            try {
                fw = new FileWriter("src/main/resources/logout.txt");
                fw.write("exit ");
                fw.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            String users[] = {"artiumdominus", "ripley", "kylereese", "connorsarah", "connorjohn", "oldbladerunner",
            "iamthebusiness", "snake", "mirrorshades", "major"};

            for (int i = 0; i < users.length; ++i) {
                // CRIAÇÃO DE GRUPO
                sql = "SELECT * FROM Grupo WHERE criador = \"" + users[i] + "\";";
                result = statement.executeQuery(sql);
                if (result.next()) {
                    try {
                        fw = new FileWriter("src/main/resources/" + users[i] + "/create_group.txt");
                        fw.write("new_g" +
                                Utils.ALFA(result.getString("groupname"), 70) +
                                Utils.ALFA(result.getString("nome"), 70) +
                                Utils.NUM(result.getString("tipo"), 1) +
                                Utils.ALFA(result.getString("descricao"), 70) +
                                "\n"
                        );
                        fw.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    // ADICIONAR PESSOA A GRUPO
                    sql = "SELECT Membro.groupname, username FROM Membro INNER JOIN Grupo ON Membro.groupname = Grupo.groupname WHERE criador = \"" + users[i] +"\";";
                    result = statement.executeQuery(sql);
                    try {
                        fw = new FileWriter("src/main/resources/" + users[i] + "/add_member.txt");
                        while (result.next()) {
                            fw.write("+mem " +
                                    Utils.ALFA(result.getString("groupname"), 70) +
                                    Utils.ALFA(result.getString("username"), 70) +
                                    "\n"
                            );
                        }
                        fw.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }

                    // PROMOVER PESSOA A ADMINISTRADOR DO GRUPO
                    sql = "SELECT Administrador.groupname, username FROM Administrador INNER JOIN Grupo ON Administrador.groupname = Grupo.groupname WHERE criador = \"" + users[i] +"\";";
                    result = statement.executeQuery(sql);
                    try {
                        fw = new FileWriter("src/main/resources/" + users[i] + "/add_admin.txt");
                        while (result.next()) {
                            fw.write("+admn" +
                                    Utils.ALFA(result.getString("groupname"), 70) +
                                    Utils.ALFA(result.getString("username"), 70) +
                                    "\n"
                            );
                        }
                        fw.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }

                }

                // ENVIO DE MENSAGEM
                sql = "SELECT * FROM Mensagem WHERE emissor = \"" + users[i] + "\";";
                result = statement.executeQuery(sql);
                try {
                    fw = new FileWriter("src/main/resources/" + users[i] + "/messages.txt");
                    while (result.next()) {
                        fw.write("send " +
                                Utils.ALFA(result.getString("receptor"), 70) +
                                Utils.ALFA(result.getString("conteudo"), 1000) +
                                "\n"
                        );
                    }
                    fw.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }

            statement.close();
            connection.close();
        }
    }

}
