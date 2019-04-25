package br.com.puc.efato.repositories;

import br.com.puc.efato.models.db.Turma;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurmaRepository extends CrudRepository<Turma, Long>  {
    Turma findByCodigo(long codigo);

    @Query("SELECT t FROM turma t WHERE (curso_codigo = ?1 OR ?1 = 0) AND (disciplina_codigo = ?2 OR ?2 = 0) AND (unidade_codigo = ?3 OR ?3 = 0) ")
    List<Turma> findByFilter(String disciplica, String curso, String unidade);



}
