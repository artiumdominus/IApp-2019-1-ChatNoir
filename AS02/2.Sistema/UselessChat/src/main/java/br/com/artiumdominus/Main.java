package br.com.artiumdominus;

import br.com.artiumdominus.base.ConexaoMySQL;
import br.com.artiumdominus.base.DataBase;

public class Main {

    public static void main(String[] args) {
        DataBase.importarLote("src/main/resources/lote_completo.json");
    }

}
