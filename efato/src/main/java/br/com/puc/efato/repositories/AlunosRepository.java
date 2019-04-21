package br.com.puc.efato.repositories;

import br.com.puc.efato.models.db.Aluno;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunosRepository extends CrudRepository<Aluno, String>  {
    Aluno findByLogin(String login);
    Aluno findByNome(String nome);
    Aluno findByCodigo(long codigo);
    List<Aluno> findByNomeContaining(String term);

}
