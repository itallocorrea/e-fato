package br.com.puc.efato.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {

    @JsonProperty("login")
    private String login;
    @JsonProperty("senha")
    private String senha;

    public LoginRequest(){}

    public LoginRequest(String login, String senha){
        this.login = login;
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
