package br.com.artiumdominus.base;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class DataBase {

    private static Connection connection;
    private static Statement statement;

    public static void createDataBase() throws Exception {
        connection = ConexaoMySQL.getConexaoMySQL();
        if (connection != null) {
            statement = connection.createStatement();

            System.out.println("Criando Banco de Dados");

            String sql = "DROP DATABASE IF EXISTS Loneliness;";

            statement.execute(sql);

            sql = "CREATE DATABASE IF NOT EXISTS Loneliness";

            statement.execute(sql);

            sql = "USE Loneliness;";

            statement.execute(sql);

            sql = "SET time_zone = \'+3:00\';";

            statement.execute(sql);

            sql = "CREATE TABLE Perfis (" +
                    "nome VARCHAR(70) NOT NULL," +
                    "username VARCHAR(70) NOT NULL," +
                    "numeroDeTelefone VARCHAR(70) NOT NULL," +
                    "bio VARCHAR(70)," +
                    "chatAddress VARCHAR(70) NOT NULL," +
                    "PRIMARY KEY (username)" +
            ");";

            statement.execute(sql);

            sql = "CREATE TABLE Grupos (" +
                    "nome VARCHAR(70) NOT NULL," +
                    "groupname VARCHAR(70) NOT NULL," +
                    "descricao VARCHAR(70)," +
                    "tipo TINYINT," +
                    "criador VARCHAR(70) NOT NULL," +
                    "chatAddress VARCHAR(70) NOT NULL," +
                    "PRIMARY KEY (groupname)" +
            ");";

            statement.execute(sql);

            sql = "CREATE TABLE Mensagens (" +
                    "id INT NOT NULL AUTO_INCREMENT," +
                    "conteudo VARCHAR(1000) NOT NULL," +
                    "envio DATETIME NOT NULL," +
                    "status VARCHAR(10)," +
                    "emissor VARCHAR(70)," +
                    "receptor VARCHAR(70)," +
                    "PRIMARY KEY (id)" +
            ");";

            statement.execute(sql);

            sql = "CREATE TABLE Membros (" +
                    "groupname VARCHAR(70) NOT NULL," +
                    "username VARCHAR(70) NOT NULL" +
            ");";

            statement.execute(sql);

            sql = "CREATE TABLE Administradores (" +
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
        JSONArray array;
        JSONObject operacao;


        if (connection != null) {
            statement = connection.createStatement();

            System.out.println("Gerando Arquivos...");

            String sql = "USE UselessChat";
            ResultSet result;

            statement.execute(sql);

            // LOGON
            array = new JSONArray();
            sql = "SELECT * FROM Perfil";

            result = statement.executeQuery(sql);
            try {
                fw = new FileWriter("src/main/resources/logon.json");
                while (result.next()) {
                    operacao = new JSONObject();
                    operacao.put("op", "logon");
                    operacao.put("username", result.getString("username"));
                    operacao.put("senha", result.getString("senha"));
                    operacao.put("nome", result.getString("nome"));
                    operacao.put("numeroDeTelefone", result.getString("numeroDeTelefone"));
                    operacao.put("bio", result.getString("bio"));
                    array.put(operacao);
                }
                array.write(fw);
                fw.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            // LOGIN
            array = new JSONArray();
            result = statement.executeQuery(sql);
            try {
                fw = new FileWriter("src/main/resources/login.json");
                while (result.next()) {
                    operacao = new JSONObject();
                    operacao.put("op", "login");
                    operacao.put("username", result.getString("username"));
                    operacao.put("senha", result.getString("senha"));
                    array.put(operacao);
                }
                array.write(fw);
                fw.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            // LOGOUT
            array = new JSONArray();
            try {
                fw = new FileWriter("src/main/resources/logout.json");
                operacao = new JSONObject();
                operacao.put("op", "logout");
                array.put(operacao);
                array.write(fw);
                fw.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            String users[] = {"artiumdominus", "ripley", "kylereese", "connorsarah", "connorjohn", "oldbladerunner",
            "iamthebusiness", "snake", "mirrorshades", "major"};

            for (int i = 0; i < users.length; ++i) {
                // CRIAÇÃO DE GRUPO
                array = new JSONArray();
                sql = "SELECT * FROM Grupo WHERE criador = \"" + users[i] + "\";";
                result = statement.executeQuery(sql);
                if (result.next()) {
                    operacao = new JSONObject();
                    try {
                        fw = new FileWriter("src/main/resources/" + users[i] + "/create_group.json");
                        operacao.put("op", "new group");
                        operacao.put("groupname", result.getString("groupname"));
                        operacao.put("nome", result.getString("nome"));
                        operacao.put("tipo", result.getInt("tipo"));
                        operacao.put("descricao", result.getString("descricao"));
                        array.put(operacao);
                        array.write(fw);
                        fw.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    // ADICIONAR PESSOA A GRUPO
                    array = new JSONArray();
                    sql = "SELECT Membro.groupname, username FROM Membro INNER JOIN Grupo ON Membro.groupname = Grupo.groupname WHERE criador = \"" + users[i] +"\";";
                    result = statement.executeQuery(sql);
                    try {
                        fw = new FileWriter("src/main/resources/" + users[i] + "/add_member.json");
                        while (result.next()) {
                            operacao = new JSONObject();
                            operacao.put("op", "add member");
                            operacao.put("groupname", result.getString("groupname"));
                            operacao.put("username", result.getString("username"));
                            array.put(operacao);
                        }
                        array.write(fw);
                        fw.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }

                    // PROMOVER PESSOA A ADMINISTRADOR DO GRUPO
                    array = new JSONArray();
                    sql = "SELECT Administrador.groupname, username FROM Administrador INNER JOIN Grupo ON Administrador.groupname = Grupo.groupname WHERE criador = \"" + users[i] +"\";";
                    result = statement.executeQuery(sql);
                    try {
                        fw = new FileWriter("src/main/resources/" + users[i] + "/add_admin.json");
                        while (result.next()) {
                            operacao = new JSONObject();
                            operacao.put("op", "add admin");
                            operacao.put("groupname", result.getString("groupname"));
                            operacao.put("username", result.getString("username"));
                            array.put(operacao);
                        }
                        array.write(fw);
                        fw.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }

                }

                // ENVIO DE MENSAGEM
                array = new JSONArray();
                sql = "SELECT * FROM Mensagem WHERE emissor = \"" + users[i] + "\";";
                result = statement.executeQuery(sql);
                try {
                    fw = new FileWriter("src/main/resources/" + users[i] + "/messages.json");
                    while (result.next()) {
                        operacao = new JSONObject();
                        operacao.put("op", "send");
                        operacao.put("receptor", result.getString("receptor"));
                        operacao.put("conteudo", result.getString("conteudo"));
                        array.put(operacao);
                    }
                    array.write(fw);
                    fw.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

            }

            statement.close();
            connection.close();
        }
    }

    public static void importarLote(String path) {
        String user = null;
        int chatAddressProfile = 1;
        char chatAdressGroup = 97;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF8"));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String lote = builder.toString();

            JSONArray array = new JSONArray(lote);

            for (int i = 0; i < array.length(); ++i) {
                JSONObject object = array.getJSONObject(i);

                String operation = object.getString("op");
                if (operation.equals("login")) {
                    String username = object.getString("username");
                    String senha = object.getString("senha");

                    connection = ConexaoMySQL.getConexaoMySQL();

                    if (connection != null) {
                        statement = connection.createStatement();

                        System.out.println("Logando no sistema.");

                        String sql = "SELECT senha FROM Perfil WHERE username = \"" + username + "\";";

                        statement.execute("USE UselessChat");
                        ResultSet result = statement.executeQuery(sql);

                        if (result.next()) {
                            String senha_registrada = result.getString("senha");
                            if (senha.equals(senha_registrada)) {
                                user = username;
                                System.out.println("Logado com sucesso.");
                            } else {
                                System.out.println("Senha incorreta.");
                            }
                        } else {
                            System.out.println("Usuário não encontrado.");
                        }

                        statement.close();
                        connection.close();
                    }

                } else if (operation.equals("logon")) {
                    String username = object.getString("username");
                    String senha = object.getString("senha");
                    String nome = object.getString("nome");
                    String numeroDeTelefone = object.getString("numeroDeTelefone");
                    String bio;
                    try {
                        bio = object.getString("bio");
                    } catch (JSONException e) {
                        bio = "";
                    }

                    connection = ConexaoMySQL.getConexaoMySQL();

                    if (connection != null) {
                        statement = connection.createStatement();

                        System.out.println("Criando novo usuário.");

                        String sql = "INSERT INTO Perfil (nome, username, senha, numeroDeTelefone, bio, chatAddress) " +
                                "VALUES (\"" + nome + "\", \"" + username + "\", \"" + senha + "\", \"" +
                                numeroDeTelefone + "\", \"" + bio + "\", \"" + chatAddressProfile + "\");";

                        statement.execute("USE UselessChat");
                        statement.executeUpdate(sql);

                        chatAddressProfile++;

                        statement.close();
                        connection.close();
                    }

                } else if (operation.equals("logout")) {
                    user = null;
                    System.out.println("Deslogado com sucesso.");
                } else if (operation.equals("send")) { // Checar se o usuário não está enviando mensagem para um grupo que ele não pertence
                    if (user != null) {
                        String destinatario = object.getString("receptor");
                        String conteudo = object.getString("conteudo");

                        connection = ConexaoMySQL.getConexaoMySQL();

                        if (connection != null) {
                            statement = connection.createStatement();

                            System.out.println("Enviando mensagem");

                            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                            String sql = "INSERT INTO Mensagem (conteudo, envio, status, emissor, receptor) VALUES " +
                                    "(\"" + conteudo + "\",\"" + format.format(new Date()) + "\", \"enviado\", \"" +
                                    user + "\", \"" + destinatario + "\");";

                            statement.execute("USE UselessChat");
                            statement.executeUpdate(sql);

                            statement.close();
                            connection.close();
                        }
                    } else {
                        System.out.println("Usuário não logado");
                    }
                } else if (operation.equals("new group")) {
                    String groupname = object.getString("groupname");
                    String nome = object.getString("nome");
                    int tipo = object.getInt("tipo");
                    String descricao = object.getString("descricao");

                    connection = ConexaoMySQL.getConexaoMySQL();

                    if (connection != null) {
                        statement = connection.createStatement();

                        System.out.println("Criando novo grupo");

                        String sql = "INSERT INTO Grupo VALUES (\"" + nome + "\", \"" + groupname + "\", \"" + descricao +
                                "\", \"" + tipo + "\", \"" + user + "\", \"" + chatAdressGroup + "\");";

                        statement.execute("USE UselessChat");
                        statement.executeUpdate(sql);

                        chatAdressGroup++;

                        sql = "INSERT INTO Membro VALUES (\"" + groupname + "\", \"" + user + "\");";
                        statement.executeUpdate(sql);
                        sql = "INSERT INTO Administrador VALUES (\"" + groupname + "\", \"" + user + "\");";
                        statement.executeUpdate(sql);

                        statement.close();
                        connection.close();
                    }

                } else if (operation.equals("add member")) { // Checar se o usuário é administrador deste grpo
                    if (user != null) {
                        String groupname = object.getString("groupname");
                        String username = object.getString("username");

                        connection = ConexaoMySQL.getConexaoMySQL();

                        if (connection != null) {
                            statement = connection.createStatement();

                            System.out.println("Adicionando novo membro");

                            String sql = "INSERT INTO Membro VALUES (\"" + groupname + "\", \"" + username + "\");";

                            statement.execute("USE UselessChat");
                            statement.executeUpdate(sql);

                            statement.close();
                            connection.close();
                        }
                    } else {
                        System.out.println("Usuário não logado");
                    }
                } else if (operation.equals("add admin")) { // Checar se o usuário é dono deste grupo
                    if (user != null) {
                        String groupname = object.getString("groupname");
                        String username = object.getString("username");

                        connection = ConexaoMySQL.getConexaoMySQL();

                        if (connection != null) {
                            statement = connection.createStatement();

                            System.out.println("Adicionando novo administrador");

                            String sql = "INSERT INTO Administrador VALUES (\"" + groupname + "\", \"" + username + "\");";

                            statement.execute("USE UselessChat");
                            statement.executeUpdate(sql);

                            statement.close();
                            connection.close();
                        }
                    } else {
                        System.out.println("Usuário não logado");
                    }
                } else if (operation.equals("revoke admin")) { // Não implementado
                    String groupname = object.getString("groupname");
                    String username = object.getString("username");

                    connection = ConexaoMySQL.getConexaoMySQL();

                    if (connection != null) {
                        statement = connection.createStatement();

                        System.out.println();

                        String sql = "";

                        ResultSet result = statement.executeQuery(sql);

                        if (result.next()) {

                        }

                        statement.close();
                        connection.close();
                    }

                } else if (operation.equals("remove member")) { // Não implementado
                    String groupname = object.getString("groupname");
                    String username = object.getString("username");

                    connection = ConexaoMySQL.getConexaoMySQL();

                    if (connection != null) {
                        statement = connection.createStatement();

                        System.out.println();

                        String sql = "";

                        ResultSet result = statement.executeQuery(sql);

                        if (result.next()) {

                        }

                        statement.close();
                        connection.close();
                    }

                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
