package br.com.puc.efato.models.db;

import javax.persistence.*;

@Entity(name = "jf")
public class JF {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long codigo;

    private String descricao;

    private float tamMaxEquipe;

    private float tempMax;

    @OneToOne
    private Disciplina disciplina;
    @OneToOne
    private Professor professor;
    @OneToOne
    private Turma turma;
    @OneToOne
    private Status status;

    public String getDescricaoStatus(){
        return status.getDescricao();
    }
    public long getCodigoStatus(){
        return status.getCodigo();
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    public float getTamMaxEquipe() {
        return tamMaxEquipe;
    }

    public void setTamMaxEquipe(float tamMaxEquipe) {
        this.tamMaxEquipe = tamMaxEquipe;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
