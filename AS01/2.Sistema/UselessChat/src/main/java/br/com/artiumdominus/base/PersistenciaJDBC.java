package br.com.artiumdominus.base;

import java.sql.Connection;
import java.sql.Statement;

public class PersistenciaJDBC {
    protected Connection connection = null;
    protected Statement stmt = null;
    protected CriaConexao criaConexao = new CriaConexao();
    protected String connectionType = "file";

    protected void preparaPersistencia() throws Exception {
        connection = criaConexao.getConnection(connectionType);
        stmt = connection.createStatement();
    }

}
