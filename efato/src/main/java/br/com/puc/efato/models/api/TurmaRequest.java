package br.com.puc.efato.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TurmaRequest {
	
	@JsonProperty("codigo")
	private Long codigo;
	
	@JsonProperty("descricao")
	private String descricao;
	
	@JsonProperty("curso_codigo")
	private long curso_codigo;
	
	@JsonProperty("disciplina_codigo")
	private long disciplina_codigo;
	
	@JsonProperty("unidade_codigo")
	private long unidade_codigo;

	@JsonProperty("alunos")
	private String aluno_nome;

	public String getAluno_nome() {
		return aluno_nome;
	}

	public void setAluno_nome(String aluno_nome) {
		this.aluno_nome = aluno_nome;
	}

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

	public long getCurso_codigo() {
		return curso_codigo;
	}

	public void setCurso_codigo(long curso_codigo) {
		this.curso_codigo = curso_codigo;
	}

	public long getDisciplina_codigo() {
		return disciplina_codigo;
	}

	public void setDisciplina_codigo(long disciplina_codigo) {
		this.disciplina_codigo = disciplina_codigo;
	}

	public long getUnidade_codigo() {
		return unidade_codigo;
	}

	public void setUnidade_codigo(long unidade_codigo) {
		this.unidade_codigo = unidade_codigo;
	}
}
