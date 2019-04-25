package br.com.puc.efato.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JulgamentoRequest {

    @JsonProperty("codigo")
    private Long codigo;

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("tamMaxEquipe")
    private Float tamMaxEquipe;

    @JsonProperty("tempMax")
    private Float tempMax;

    @JsonProperty("disciplina_codigo")
    private int disciplina_codigo;

    @JsonProperty("professor_codigo")
    private int professor_codigo;

    @JsonProperty("turma_codigo")
    private int turma_codigo;

    @JsonProperty("status_codigo")
    private int status_codigo;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Float getTamMaxEquipe() {
        return tamMaxEquipe;
    }

    public void setTamMaxEquipe(Float tamMaxEquipe) {
        this.tamMaxEquipe = tamMaxEquipe;
    }

    public Float getTempMax() {
        return tempMax;
    }

    public void setTempMax(Float tempMax) {
        this.tempMax = tempMax;
    }

    public int getDisciplina_codigo() {
        return disciplina_codigo;
    }

    public void setDisciplina_codigo(int disciplina_codigo) {
        this.disciplina_codigo = disciplina_codigo;
    }

    public int getProfessor_codigo() {
        return professor_codigo;
    }

    public void setProfessor_codigo(int professor_codigo) {
        this.professor_codigo = professor_codigo;
    }

    public int getTurma_codigo() {
        return turma_codigo;
    }

    public void setTurma_codigo(int turma_codigo) {
        this.turma_codigo = turma_codigo;
    }

    public int getStatus_codigo() {
        return status_codigo;
    }

    public void setStatus_codigo(int status_codigo) {
        this.status_codigo = status_codigo;
    }
}
