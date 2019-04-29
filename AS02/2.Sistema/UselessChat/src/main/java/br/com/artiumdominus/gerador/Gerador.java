package br.com.artiumdominus.gerador;

import br.com.artiumdominus.base.DataBase;

import java.sql.SQLException;

public class Gerador {

    public static void main(String[] args) {
        try {
            DataBase.gerarArquivosDeImportacao();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
