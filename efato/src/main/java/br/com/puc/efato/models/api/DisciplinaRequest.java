package br.com.puc.efato.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DisciplinaRequest {
	
	@JsonProperty("codigo")
	private Long codigo;
	
	@JsonProperty("descricao")
	private String descricao;

	@JsonProperty("professor_codigo")
	private int professor_codigo;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getProfessor_codigo() {
		return professor_codigo;
	}

	public void setProfessor_codigo(int professor_codigo) {
		this.professor_codigo = professor_codigo;
	}
}
