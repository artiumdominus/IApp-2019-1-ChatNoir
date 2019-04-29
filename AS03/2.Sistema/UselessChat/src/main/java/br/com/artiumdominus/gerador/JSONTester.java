package br.com.artiumdominus.gerador;

import org.json.JSONArray;

public class JSONTester {

    public static void main(String[] args) {
        JSONArray array = new JSONArray();
        array.put(1);
        array.put(2);
        array.put("a");
        array.put("true");
        System.out.println(array);

        array = new JSONArray("[1,2,\"a\",\"true\"]");

        for (int i = 0; i < 4; i++) {
            System.out.println(i + " : " + array.get(i));
        }
    }
}
