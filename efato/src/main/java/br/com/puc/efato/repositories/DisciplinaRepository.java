package br.com.puc.efato.repositories;

import br.com.puc.efato.models.db.Disciplina;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplinaRepository extends CrudRepository<Disciplina, String>  {
    Disciplina findByCodigo(long descricao);
    Disciplina findByDescricao(String descricao);
}
