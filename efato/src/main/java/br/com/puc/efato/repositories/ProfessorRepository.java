package br.com.puc.efato.repositories;

import br.com.puc.efato.models.db.Professor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends CrudRepository<Professor, String>  {
    Professor findByLogin(String login);
}
