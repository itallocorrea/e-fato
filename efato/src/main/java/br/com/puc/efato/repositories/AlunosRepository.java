package br.com.puc.efato.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.puc.efato.models.db.Aluno;

@Repository
public interface AlunosRepository extends CrudRepository<Aluno, Integer>  {

}
