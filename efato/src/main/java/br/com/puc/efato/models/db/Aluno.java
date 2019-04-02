package br.com.puc.efato.models.db;

import br.com.puc.efato.models.api.UsuarioRequest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "aluno")
public class Aluno implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long codigo;
	
	private String nome;
	
	private String email;
	
	private String login;
	
	private String senha;

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

	public Aluno(){

	}

	public Aluno(UsuarioRequest usuarioRequest){
		this.nome = usuarioRequest.getNome();
		this.email = usuarioRequest.getEmail();
		this.login = usuarioRequest.getLogin();
		this.senha = usuarioRequest.getSenha();
	}

}
