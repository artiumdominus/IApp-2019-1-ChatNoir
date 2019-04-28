package br.com.artiumdominus;

import br.com.artiumdominus.base.ConexaoMySQL;
import br.com.artiumdominus.base.DataBase;

public class Main {

    public static void main(String[] args) {

        // Teste de conectividade
        /*
        System.out.println(ConexaoMySQL.statusConnection());
        ConexaoMySQL.getConexaoMySQL();
        System.out.println(ConexaoMySQL.statusConnection());
        ConexaoMySQL.fecharConexao();
        System.out.println(ConexaoMySQL.statusConnection());
        */

        DataBase.importarLote("src/main/resources/lote_completo.txt");
    }

}
