package br.com.artiumdominus.model;

import java.util.ArrayList;
import java.util.List;

public class Grupo {

    private String nome;
    private String groupname;
    private String descricao;
    private Tipo tipo;
    private Chat chat;
    private Perfil criador;
    private List<Perfil> membros;
    private List<Perfil> administradores;

    public Grupo(){
        membros = new ArrayList<Perfil>();
        administradores = new ArrayList<Perfil>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Perfil getCriador() {
        return criador;
    }

    public void setCriador(Perfil criador) {
        this.criador = criador;
    }

    public List<Perfil> getMembros() {
        return membros;
    }

    public void setMembros(List<Perfil> membros) {
        this.membros = membros;
    }

    public List<Perfil> getAdministradores() {
        return administradores;
    }

    public void setAdministradores(List<Perfil> administradores) {
        this.administradores = administradores;
    }
}
