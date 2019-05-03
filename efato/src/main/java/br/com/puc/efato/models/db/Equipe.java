package br.com.puc.efato.models.db;

import javax.persistence.*;
import java.util.List;

@Entity(name = "equipe")
public class Equipe {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long codigo;

    @ManyToOne
    private Aluno alunoLider;
    
    @ManyToOne
    private JF jf;

    @ManyToMany
    @JoinTable(
            name = "membros_equipe",
            joinColumns = @JoinColumn(name = "Equipe_codigo"),
            inverseJoinColumns = @JoinColumn(name = "Aluno_codigo")
    )


    private List<Aluno> alunos;

	public long getCodigo() {
		return codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public Aluno getAlunoLider() {
		return alunoLider;
	}

	public void setAlunoLider(Aluno alunoLider) {
		this.alunoLider = alunoLider;
	}

	public List<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}

	public JF getJf() {
		return jf;
	}

	public void setJf(JF jf) {
		this.jf = jf;
	}
}
