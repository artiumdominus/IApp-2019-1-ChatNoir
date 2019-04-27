package br.com.artiumdominus.model;

import java.time.LocalDateTime;

public class Mensagem {

    private Integer id;
    private String conteudo;
    private LocalDateTime envio;
    private Status status;
    private Perfil emissor;

    public Mensagem(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public LocalDateTime getEnvio() {
        return envio;
    }

    public void setEnvio(LocalDateTime envio) {
        this.envio = envio;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Perfil getEmissor() {
        return emissor;
    }

    public void setEmissor(Perfil emissor) {
        this.emissor = emissor;
    }

}
