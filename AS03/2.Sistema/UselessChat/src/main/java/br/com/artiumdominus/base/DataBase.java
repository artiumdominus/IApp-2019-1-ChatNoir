package br.com.artiumdominus.base;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

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
        XMLWriter writer;
        Element root;
        Element operacao;

        Document outputDocument;
        OutputFormat format = OutputFormat.createPrettyPrint();

        if (connection != null) {
            statement = connection.createStatement();

            System.out.println("Gerando Arquivos...");

            String sql = "USE UselessChat";
            ResultSet result;

            statement.execute(sql);

            // LOGON
            outputDocument = DocumentHelper.createDocument();
            root = outputDocument.addElement("logon");

            sql = "SELECT * FROM Perfil";

            result = statement.executeQuery(sql);
            try {
                writer = new XMLWriter(new FileWriter("src/main/resources/logon.xml"), format);
                while (result.next()) {
                    operacao = root.addElement("operacao");
                    operacao.addElement("op").addText("logon");
                    operacao.addElement("username").addText(result.getString("username"));
                    operacao.addElement("senha").addText(result.getString("senha"));
                    operacao.addElement("nome").addText(result.getString("nome"));
                    operacao.addElement("numero-de-telefone").addText(result.getString("numeroDeTelefone"));
                    String bio = result.getString("bio");
                    if (bio != null) {
                        operacao.addElement("bio").addText(bio);
                    }
                }
                writer.write(outputDocument);
                writer.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            // LOGIN
            outputDocument = DocumentHelper.createDocument();
            root = outputDocument.addElement("login");

            result = statement.executeQuery(sql);
            try {
                writer = new XMLWriter(new FileWriter("src/main/resources/login.xml"), format);
                while (result.next()) {
                    operacao = root.addElement("operacao");
                    operacao.addElement("op").addText("login");
                    operacao.addElement("username").addText(result.getString("username"));
                    operacao.addElement("senha").addText(result.getString("senha"));
                }
                writer.write(outputDocument);
                writer.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            // LOGOUT
            outputDocument = DocumentHelper.createDocument();
            root = outputDocument.addElement("logout");
            try {
                writer = new XMLWriter(new FileWriter("src/main/resources/logout.xml"), format);
                operacao = root.addElement("operacao");
                operacao.addElement("op").addText("logout");
                writer.write(outputDocument);
                writer.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            String users[] = {"artiumdominus", "ripley", "kylereese", "connorsarah", "connorjohn", "oldbladerunner",
            "iamthebusiness", "snake", "mirrorshades", "major"};

            for (int i = 0; i < users.length; ++i) {
                // CRIAÇÃO DE GRUPO
                outputDocument = DocumentHelper.createDocument();
                root = outputDocument.addElement("new-group");
                sql = "SELECT * FROM Grupo WHERE criador = \"" + users[i] + "\";";
                result = statement.executeQuery(sql);
                if (result.next()) {
                    try {
                        writer = new XMLWriter(new FileWriter("src/main/resources/" + users[i] + "/create_group.xml"), format);
                        operacao = root.addElement("operacao");
                        operacao.addElement("op").addText("new group");
                        operacao.addElement("groupname").addText(result.getString("groupname"));
                        operacao.addElement("nome").addText(result.getString("nome"));
                        operacao.addElement("tipo").addText(result.getString("tipo"));
                        operacao.addElement("descricao").addText(result.getString("descricao"));
                        writer.write(outputDocument);
                        writer.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    // ADICIONAR PESSOA A GRUPO
                    outputDocument = DocumentHelper.createDocument();
                    root = outputDocument.addElement("add-member");
                    sql = "SELECT Membro.groupname, username FROM Membro INNER JOIN Grupo ON Membro.groupname = Grupo.groupname WHERE criador = \"" + users[i] +"\";";
                    result = statement.executeQuery(sql);
                    try {
                        writer = new XMLWriter(new FileWriter("src/main/resources/" + users[i] + "/add_member.xml"), format);
                        while (result.next()) {
                            operacao = root.addElement("operacao");
                            operacao.addElement("op").addText("add member");
                            operacao.addElement("groupname").addText(result.getString("groupname"));
                            operacao.addElement("username").addText(result.getString("username"));
                        }
                        writer.write(outputDocument);
                        writer.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }

                    // PROMOVER PESSOA A ADMINISTRADOR DO GRUPO
                    outputDocument = DocumentHelper.createDocument();
                    root = outputDocument.addElement("add-admin");
                    sql = "SELECT Administrador.groupname, username FROM Administrador INNER JOIN Grupo ON Administrador.groupname = Grupo.groupname WHERE criador = \"" + users[i] +"\";";
                    result = statement.executeQuery(sql);
                    try {
                        writer = new XMLWriter(new FileWriter("src/main/resources/" + users[i] + "/add_admin.xml"), format);
                        while (result.next()) {
                            operacao = root.addElement("operacao");
                            operacao.addElement("op").addText("add admin");
                            operacao.addElement("groupname").addText(result.getString("groupname"));
                            operacao.addElement("username").addText(result.getString("username"));
                        }
                        writer.write(outputDocument);
                        writer.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }

                }

                // ENVIO DE MENSAGEM
                outputDocument = DocumentHelper.createDocument();
                root = outputDocument.addElement("send-message");
                sql = "SELECT * FROM Mensagem WHERE emissor = \"" + users[i] + "\";";
                result = statement.executeQuery(sql);
                try {
                    writer = new XMLWriter(new FileWriter("src/main/resources/" + users[i] + "/messages.xml"), format);
                    while (result.next()) {
                        operacao = root.addElement("operacao");
                        operacao.addElement("op").addText("send");
                        operacao.addElement("destinatario").addText(result.getString("receptor"));
                        operacao.addElement("conteudo").addText(result.getString("conteudo"));
                    }
                    writer.write(outputDocument);
                    writer.close();
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
            File inputFile = new File(path);
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputFile);

            System.out.println("Root element :" + document.getRootElement().getName());

            List<Node> operacoes = document.selectNodes("/lote/operacao");

            for (Node operacao : operacoes) {

                String operation = operacao.selectSingleNode("op").getText();
                if (operation.equals("login")) {
                    String username = operacao.selectSingleNode("username").getText();
                    String senha = operacao.selectSingleNode("senha").getText();

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
                    String username = operacao.selectSingleNode("username").getText();
                    String senha = operacao.selectSingleNode("senha").getText();
                    String nome = operacao.selectSingleNode("nome").getText();
                    String numeroDeTelefone = operacao.selectSingleNode("numero-de-telefone").getText();
                    String bio;
                    try {
                        bio = operacao.selectSingleNode("bio").getText();
                    } catch (NullPointerException e) {
                        bio = null;
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
                        String destinatario = operacao.selectSingleNode("destinatario").getText();
                        String conteudo = operacao.selectSingleNode("conteudo").getText();

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
                    String groupname = operacao.selectSingleNode("groupname").getText();
                    String nome = operacao.selectSingleNode("nome").getText();
                    int tipo = Integer.parseInt(operacao.selectSingleNode("tipo").getText());
                    String descricao = operacao.selectSingleNode("descricao").getText();

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
                        String groupname = operacao.selectSingleNode("groupname").getText();
                        String username = operacao.selectSingleNode("username").getText();

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
                        String groupname = operacao.selectSingleNode("groupname").getText();
                        String username = operacao.selectSingleNode("username").getText();

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
                    String groupname = operacao.selectSingleNode("groupname").getText();
                    String username = operacao.selectSingleNode("username").getText();

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
                    String groupname = operacao.selectSingleNode("groupname").getText();
                    String username = operacao.selectSingleNode("username").getText();

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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (DocumentException e) {
            System.out.println(e.getMessage());
        }
    }

}
