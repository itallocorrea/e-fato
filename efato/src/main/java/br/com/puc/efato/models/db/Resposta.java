package br.com.puc.efato.models.db;

import javax.persistence.Entity;

@Entity(name = "resposta")
public class Resposta {
    private Fato fato;
    private Equipe equipe;
    private Boolean resposta;

    public Fato getFato() {
        return fato;
    }

    public void setFato(Fato fato) {
        this.fato = fato;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public Boolean getResposta() {
        return resposta;
    }

    public void setResposta(Boolean resposta) {
        this.resposta = resposta;
    }
}
