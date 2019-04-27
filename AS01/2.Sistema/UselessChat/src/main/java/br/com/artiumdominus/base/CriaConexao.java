package br.com.artiumdominus.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CriaConexao {

    public static final String JDBC_DRIVER = "org.h2.Driver";

    public static final String DB_FILE_URL = "jdbc:h2~/loneliness";

    public static final String DB_MEMORY_URL = "jdbc:h2:mem:testdb";

    public static final String USER = "sa";

    public static final String PASS = "";

    private Connection conn = null;

    public Connection getConnection(String connectionType) throws InvalidDataBaseTypeException {
        try {
            Class.forName(JDBC_DRIVER);

            if(!connectionType.equalsIgnoreCase("file") && !connectionType.equalsIgnoreCase("memory")) {
                throw new InvalidDataBaseTypeException("O tipo do banco de dados deve ser [file] ou [memory].");
            } else if (connectionType.equalsIgnoreCase("file")) {
                conn = DriverManager.getConnection(DB_FILE_URL, USER, PASS);
            } else {
                conn = DriverManager.getConnection(DB_MEMORY_URL, USER, PASS);
            }
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
