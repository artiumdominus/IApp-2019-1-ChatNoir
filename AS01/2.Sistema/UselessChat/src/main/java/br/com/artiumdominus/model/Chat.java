package br.com.artiumdominus.model;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    private String address;
    private List<Mensagem> mensagens;

    public Chat(){
        mensagens = new ArrayList<Mensagem>();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setMensagens(List<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }
}
