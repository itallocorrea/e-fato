package br.com.puc.efato.repositories;

import br.com.puc.efato.models.db.Curso;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends CrudRepository<Curso, String>  {
    Curso findByCodigo(long curso);
}
