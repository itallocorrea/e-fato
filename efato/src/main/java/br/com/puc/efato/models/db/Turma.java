package br.com.puc.efato.models.db;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity(name = "turma")
public class Turma {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long codigo;

    @OneToOne
    private Disciplina disciplina;
    @OneToOne
    private Curso curso;
    @OneToOne
    private Unidade unidade;

    @ManyToMany
    @JoinTable(
            name = "classe",
            joinColumns = @JoinColumn(name = "Turma_codigo"),
            inverseJoinColumns = @JoinColumn(name = "Aluno_codigo")
    )
    private List<Aluno> alunos;

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(Aluno aluno) {
        if(alunos.isEmpty())
            alunos = new ArrayList<>();
        if(!alunos.contains(aluno))
            this.alunos.add(aluno);
    }

    public String getDescricaoDisciplina(){
        return disciplina.getDescricao();
    }
    public String getDescricaoCurso(){
        return curso.getDescricao();
    }
    public String getDescricaoUnidade(){
        return unidade.getDescricao();
    }


    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }
}
