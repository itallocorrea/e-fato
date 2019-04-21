package br.com.puc.efato.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsuarioRequest {
	
	@JsonProperty("codigo")
	private Long codigo;
	
	@JsonProperty("nome")
	private String nome;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("login")
	private String login;
	
	@JsonProperty("senha")
	private String senha;
	
	@JsonProperty("curso")
	private long curso;

	@JsonProperty("tipo")
	private String tipo;
	
	public long getCodigo() {
		return codigo;
	}
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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

	public long getCurso() {
		return curso;
	}

	public void setCurso(long curso) {
		this.curso = curso;
	}

	public String getTipo() { return tipo; }
	public void setTipo(String tipo) { this.tipo = tipo; }
}
