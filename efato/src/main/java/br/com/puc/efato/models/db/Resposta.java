package br.com.puc.efato.models.db;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "resposta")
public class Resposta implements Serializable {

	private static final long serialVersionUID = 2543888187236474579L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer codigo;
	
	private Integer codigoEquipe;
	
	private Integer codigoFato;
	
	private Boolean resposta;

	public Integer getCodigoEquipe() {
		return codigoEquipe;
	}

	public void setCodigoEquipe(Integer codigoEquipe) {
		this.codigoEquipe = codigoEquipe;
	}

	public Integer getCodigoFato() {
		return codigoFato;
	}

	public void setCodigoFato(Integer codigoFato) {
		this.codigoFato = codigoFato;
	}

	public Boolean getResposta() {
		return resposta;
	}

	public void setResposta(Boolean resposta) {
		this.resposta = resposta;
	}
}