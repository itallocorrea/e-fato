package br.com.puc.efato.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MembrosRequest {

	@JsonProperty("codigo_equipe")
	private Long codigo_equipe;

	@JsonProperty("aluno_nome")
	private String aluno_nome;

	public Long getCodigo_equipe() {
		return codigo_equipe;
	}

	public void setCodigo_equipe(Long codigo_equipe) {
		this.codigo_equipe = codigo_equipe;
	}

	public String getAluno_nome() {
		return aluno_nome;
	}

	public void setAluno_nome(String aluno_nome) {
		this.aluno_nome = aluno_nome;
	}
}
